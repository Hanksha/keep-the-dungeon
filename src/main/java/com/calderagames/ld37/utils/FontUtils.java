package com.calderagames.ld37.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class FontUtils {

    private FontUtils() {}

    public static class FontParamBuilder {

        private FreetypeFontLoader.FreeTypeFontLoaderParameter param;

        public FontParamBuilder(String fileName) {
            param = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
            param.fontFileName = fileName;
        }

        public FontParamBuilder setSize(int size) {
            param.fontParameters.size = size;
            return this;
        }

        public FontParamBuilder setBorderWidth(int width) {
            param.fontParameters.borderWidth = width;
            return this;
        }

        public FontParamBuilder setSmoothing(boolean smooth) {
            param.fontParameters.mono = !smooth;
            return this;
        }

        public FontParamBuilder setTextureFilter(Texture.TextureFilter filter) {
            param.fontParameters.minFilter = filter;
            param.fontParameters.magFilter = filter;
            return this;
        }

        public FreetypeFontLoader.FreeTypeFontLoaderParameter build() {
            return param;
        }

    }
}
