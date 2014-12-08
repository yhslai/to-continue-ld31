package cc.raintomorrow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class EcgApp extends ApplicationAdapter {
	private SpriteBatch batch;
    private Stage stage;
    private AssetManager assetManager;

    FPSLogger fpsLogger = new FPSLogger();

	@Override
	public void create () {
        Ecg.app = this;
        Ecg.state = new EcgState();

		this.batch = new SpriteBatch();

        AssetManager manager = new AssetManager();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class,  new FreetypeFontLoader(resolver));

        manager.load("img/bumper.png", Texture.class);
        manager.load("img/bonus.png", Texture.class);
        manager.load("img/eyelid-mask.png", Texture.class);
        manager.load("img/bully.png", Texture.class);
        manager.load("img/background.png", Texture.class);
        manager.load("img/cutscene-background.png", Texture.class);
        manager.load("img/pack.atlas", TextureAtlas.class);
        manager.load("sound/beat.wav", Sound.class);
        manager.load("sound/bonus.wav", Sound.class);
        manager.load("sound/die.wav", Sound.class);
        manager.load("sound/hit-1.wav", Sound.class);
        manager.load("sound/hit-2.wav", Sound.class);
        manager.load("sound/shoot.wav", Sound.class);
        manager.load("sound/warn.wav", Sound.class);
        manager.load("sound/type.wav", Sound.class);

        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParams.fontFileName = "font/believer.ttf";
        fontParams.fontParameters.size = 42;
        manager.load("font/believer.ttf.42", BitmapFont.class, fontParams);

        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParams2 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParams2.fontFileName = "font/believer.ttf";
        fontParams2.fontParameters.size = 32;
        manager.load("font/believer.ttf.32", BitmapFont.class, fontParams2);

        manager.finishLoading();
        this.assetManager = manager;
        Sounds.loadSounds();

        this.stage = new EcgStage(new FitViewport(960, 720), batch);
        Gdx.input.setInputProcessor(stage);
	}

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
	public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(deltaTime);
        stage.draw();

        fpsLogger.log();
	}

    public <T> T getAsset(String fileName) {
        return assetManager.get(fileName);
    }

    public <T> T getAsset(String fileName, Class<T> type) {
        return assetManager.get(fileName, type);
    }
}
