package cc.raintomorrow;

import cc.raintomorrow.cutscene.Cutscene;
import cc.raintomorrow.cutscene.CutsceneManager;
import cc.raintomorrow.cutscene.RunnableCutscene;
import cc.raintomorrow.cutscene.TextCutscene;
import cc.raintomorrow.graphics.Animator;
import cc.raintomorrow.graphics.AnimatorBuilder;
import cc.raintomorrow.phase.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class EcgStage extends Stage {
    static public float DEFAULT_BUMPER_RANGE = 360f;
    static public float DEFAULT_HP_CONSUMPTION = 75;

    protected float scrollX = 0;

    protected WaveActor wave;
    protected BumperActor topBumper;
    protected BumperActor bottomBumper;
    protected HpBarActor hpBar;

    protected ArrayList<HpUpdater> hpUpdaters = new ArrayList<HpUpdater>();

    protected Phase[] phases;
    protected int phaseIndex = -1;
    protected Phase currentPhase;

    private Animator eyelidOpenMask;
    private Animator eyelidCloseMask;
    private Animator eyelidHalfOpenMask;
    private Animator eyelidHalfCloseMask;
    private boolean eyesOpening;
    private boolean eyesClosing;
    private boolean eyesHalfOpening;
    private boolean eyesHalfClosing;
    private Texture background;
    private CutsceneManager cutsceneManager;

    public EcgStage(Viewport viewport, Batch batch) {
        super(viewport, batch);

        this.wave = new WaveActor();
        addActor(wave);

        this.topBumper = new BumperActor(getHeight()/2+ DEFAULT_BUMPER_RANGE /2, wave);
        this.bottomBumper = new BumperActor(getHeight()/2- DEFAULT_BUMPER_RANGE /2, wave);
        topBumper.setAnother(bottomBumper);
        bottomBumper.setAnother(topBumper);
        addActor(topBumper);
        addActor(bottomBumper);

        this.hpBar = new HpBarActor();
        addActor(hpBar);

        addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(!cutsceneManager.isEnded()) {
                    cutsceneManager.pressAction();
                }
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(!cutsceneManager.isEnded()) {
                    cutsceneManager.pressAction();
                }
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(isGameRunning()) {
                    switch (keycode) {
                        case Input.Keys.UP:
                        case Input.Keys.W:
                            wave.goUp();
                            break;
                        case Input.Keys.DOWN:
                        case Input.Keys.S:
                            wave.goDown();
                            break;
                        case Input.Keys.RIGHT:
                        case Input.Keys.LEFT:
                        case Input.Keys.D:
                        case Input.Keys.A:
                            wave.setHorizontal(true);
                            break;
                        case Input.Keys.Z:
                            if(Ecg.DEBUG)
                                Ecg.state.changeHpBy(100);
                            break;
                        case Input.Keys.X:
                            if(Ecg.DEBUG)
                                Ecg.state.changeHpBy(-100);
                            break;
                    }
                }
                return false;
            }
        });

        Texture texture = Ecg.app.getAsset("img/eyelid-mask.png");
        AnimatorBuilder animBuilder = new AnimatorBuilder(texture, 2, 5);
        this.eyelidOpenMask = animBuilder.buildAnimator(0.075f, 0, 10, Animation.PlayMode.NORMAL);
        this.eyelidCloseMask = animBuilder.buildAnimator(0.075f, 9, -1, Animation.PlayMode.NORMAL);
        this.eyelidHalfOpenMask = animBuilder.buildAnimator(0.15f, 0, 5, Animation.PlayMode.NORMAL);
        this.eyelidHalfCloseMask = animBuilder.buildAnimator(0.15f, 4, -1, Animation.PlayMode.NORMAL);

        this.background = Ecg.app.getAsset("img/background.png");

        this.cutsceneManager = new CutsceneManager(this);

        this.phases = new Phase[] {
                new SchoolPhase(this),
                new ExamPhase(this),
                new BullyingPhase(this),
                new FirstLovePhase(this),
                new RejectionPhase(this),
                new MarriagePhase(this),
        };
        nextPhase();
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);

        if(actor instanceof EcgActor) {
            EcgActor ecgActor = (EcgActor)actor;
            ecgActor.onAdded();
        }
    }

    public float getScrollX() {
        return scrollX;
    }

    public WaveActor getWave() {
        return wave;
    }

    public BumperActor getTopBumper() {
        return topBumper;
    }

    public BumperActor getBottomBumper() {
        return bottomBumper;
    }

    public void reset() {
        wave.setPosition(new Vector2(350 + scrollX, getHeight() / 2));
        wave.clearTexture();
        topBumper.activate();
        bottomBumper.inactivate();
    }

    public void setHpConsumption(float amount) {
        hpUpdaters.clear();
        hpUpdaters.add(new HpUpdater(-amount));
    }

    public void switchPhase(final Phase phase) {
        if(currentPhase == null) {
            openEyes(new Runnable() {
                @Override
                public void run() {
                    phase.start();
                }
            });
            reset();
            phase.init();
        }
        else {
            currentPhase.end();
            openEyes(new Runnable() {
                @Override
                public void run() {
                    phase.start();
                }
            });
            phase.init();
        }

        currentPhase = phase;
    }

    public boolean isGameRunning() {
        return cutsceneManager.isEnded() && currentPhase != null && currentPhase.isRunning() &&
                !eyesClosing && !eyesOpening;
    }

    public void openEyes(final Runnable callback) {
        eyesOpening = true;
        eyelidOpenMask.restart();
        eyelidOpenMask.setCallback(new Runnable() {
            @Override
            public void run() {
                eyesOpening = false;
                callback.run();
            }
        });
    }

    public void closeEyes(final Runnable callback) {
        eyesClosing = true;
        eyelidCloseMask.restart();
        eyelidCloseMask.setCallback(new Runnable() {
            @Override
            public void run() {
                eyesClosing = false;
                callback.run();
            }
        });
    }

    public void openHalfEyes(final Runnable callback) {
        eyesHalfOpening = true;
        eyelidHalfOpenMask.restart();
        eyelidHalfOpenMask.setCallback(new Runnable() {
            @Override
            public void run() {
                eyesHalfOpening = false;
                callback.run();
            }
        });
    }

    public void closeHalfEyes(final Runnable callback) {
        eyesHalfClosing = true;
        eyelidHalfCloseMask.restart();
        eyelidHalfCloseMask.setCallback(new Runnable() {
            @Override
            public void run() {
                eyesHalfClosing = false;
                callback.run();
            }
        });
    }

    public void blink(Color color, float duration) {

    }

    @Override
    public void draw() {
        Batch batch = getBatch();
        if(currentPhase != null) {
            batch.begin();
            batch.draw(background, 0, 0);
            batch.end();

            super.draw();
        }

        batch.begin();
        if(eyesOpening) {
            batch.draw(eyelidOpenMask.getKeyFrame(), 0, 0, getWidth(), getHeight());
        }
        if(eyesClosing) {
            batch.draw(eyelidCloseMask.getKeyFrame(), 0, 0, getWidth(), getHeight());
        }
        if(eyesHalfOpening) {
            batch.draw(eyelidHalfOpenMask.getKeyFrame(), 0, 0, getWidth(), getHeight());
        }
        if(eyesHalfClosing) {
            batch.draw(eyelidHalfCloseMask.getKeyFrame(), 0, 0, getWidth(), getHeight());
        }
        if(!cutsceneManager.isEnded())
            cutsceneManager.draw(batch);
        batch.end();
    }

    float warnTimer=0;
    @Override
    public void act(float deltaTime) {
        if(isGameRunning()) {
            super.act(deltaTime);

            currentPhase.update(deltaTime);
            scrollX += wave.getSpeed().x * deltaTime;

            Iterator<HpUpdater> hpIt = hpUpdaters.iterator();
            while(hpIt.hasNext()) {
                HpUpdater hpUpdater = hpIt.next();
                Ecg.state.changeHpBy(hpUpdater.update(deltaTime));
                if(hpUpdater.isEnded()) hpIt.remove();
            }

            if(Ecg.state.hp <= 0) {
                gameOver();
            }
            else if(Ecg.state.hp >= Ecg.state.MAX_HP) {
                nextPhase();
            }
            else if(Ecg.state.hp <= Ecg.state.MAX_HP / 3) {
                warnTimer -= deltaTime;
                if(warnTimer < 0) {
                    warnTimer = 1.5f * Ecg.state.hp / (Ecg.state.MAX_HP / 3) + 0.25f;
                    Sounds.warn.play();
                }
            }
        }

        if(eyesOpening)
            eyelidOpenMask.update(deltaTime);
        if(eyesClosing)
            eyelidCloseMask.update(deltaTime);
        if(eyesHalfOpening)
            eyelidHalfOpenMask.update(deltaTime);
        if(eyesHalfClosing)
            eyelidHalfCloseMask.update(deltaTime);

        if(!cutsceneManager.isEnded())
            cutsceneManager.update(deltaTime);
    }

    private void nextPhase() {
        phaseIndex++;
        if(phaseIndex < phases.length) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    final Phase phase = phases[phaseIndex];
                    List<Cutscene> cutscenes = new ArrayList<Cutscene>(Arrays.asList(phase.cutscenes));
                    cutscenes.add(new RunnableCutscene(
                            new Runnable() {
                                @Override
                                public void run() {
                                    switchPhase(phase);
                                }
                            }
                    ));
                    cutsceneManager = new CutsceneManager(EcgStage.this, cutscenes.toArray(new Cutscene[cutscenes.size()]));
                }
            };
            if(currentPhase == null)
                runnable.run();
            else
                closeEyes(runnable);
        }
        else {
            gameCompleted();
        }
    }

    private void gameOver() {
        Sounds.hit1.stop();
        Sounds.hit2.stop();
        Sounds.beat.stop();
        Sounds.bonus.stop();
        Sounds.die.play(1.5f);
        closeEyes(new Runnable() {
            @Override
            public void run() {
                cutsceneManager = new CutsceneManager(EcgStage.this,
                        new TextCutscene("I'm sorry. He's gone.", "Continue ?"),
                        new RunnableCutscene(new Runnable() {
                            @Override
                            public void run() {
                                Phase tmp = currentPhase;
                                currentPhase = null;
                                switchPhase(tmp);
                                reset();
                            }
                        })
                );
            }
        });
    }

    private void gameCompleted() {
        closeEyes(new Runnable() {
            @Override
            public void run() {
                currentPhase = null;
                cutsceneManager = new CutsceneManager(EcgStage.this,
                        new TextCutscene("Please Joey,\nplease don't leave me!"),
                        new TextCutscene("I'm sorry.\n He's..."),
                        new RunnableCutscene(new Runnable() {
                            @Override
                            public void run() {
                                openHalfEyes(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeHalfEyes(new Runnable() {
                                            @Override
                                            public void run() {
                                                cutsceneManager = new CutsceneManager(EcgStage.this,
                                                        new TextCutscene("Wait a second..."),
                                                        new RunnableCutscene(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                openEyes(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        cutsceneManager = new CutsceneManager(EcgStage.this, false,
                                                                                new TextCutscene("\"(cough cough)\"", Color.BLACK),
                                                                                new TextCutscene("\"Haha...\nWill the wedding be postponed?\"", Color.BLACK));
                                                                    }
                                                                });
                                                            }
                                                        })
                                                );
                                            }
                                        });
                                    }
                                });
                            }
                        }));
            }
        });

    }

    public void addHpUpdater(HpUpdater updater) {
        hpUpdaters.add(updater);
    }
}
