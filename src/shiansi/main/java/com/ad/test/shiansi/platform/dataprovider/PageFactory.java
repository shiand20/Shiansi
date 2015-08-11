package com.ad.test.shiansi.platform.dataprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

class PageFactory {
    static Page getPage(InputStream in) throws IOException {
        Constructor constructor = new Constructor(Page.class);

        TypeDescription typeDesc = new TypeDescription(Page.class);
        typeDesc.putListPropertyType("pageValidators", String.class);
        typeDesc.putMapPropertyType("elements", String.class, HtmlElement.class);
        constructor.addTypeDescription(typeDesc);

        Yaml yamlFile = new Yaml(constructor);
        Page page = (Page) yamlFile.load(new BufferedReader(new InputStreamReader(in, "UTF-8")));

        try {
            in.close();
        } catch (IOException e) {
            // NOSONAR Do Nothing
        }

        return page;
    }
}
