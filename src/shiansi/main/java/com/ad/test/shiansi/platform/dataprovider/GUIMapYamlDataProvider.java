package com.ad.test.shiansi.platform.dataprovider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

import com.ad.test.shiansi.platform.utilities.YamlResource;


public class GUIMapYamlDataProvider implements DataProvider {

    private Yaml yaml;
    private String defaultLocale;
    private boolean isPageYamlV2;
    
    List<Object> allObjects;
    
    GUIMapYamlDataProvider() {
        //hide this constructor
    }

    public GUIMapYamlDataProvider(String fileName) throws IOException {
        isPageYamlV2 = false;
        defaultLocale = DEFAULT_LOCALE;
        allObjects = new ArrayList<Object>();

        YamlResource resource = new YamlResource(fileName);

        processAsPageYamlV2(resource);
        if (! isPageYamlV2) { 
            processAsPageYamlV1(resource);
        }

    }

    private void processAsPageYamlV1(YamlResource resource) throws IOException {
        if (allObjects.size() == 0) {
            InputStream input = resource.getInputStream();

            yaml = new Yaml();

            Iterable<Object> it = yaml.loadAll(input);
            for (Object temp : it) {
                allObjects.add(temp);
            }
            input.close();
        }
    }

    private void processAsPageYamlV2(YamlResource resource) throws IOException {
        InputStream input = resource.getInputStream();
        try {
            Page page = PageFactory.getPage(input);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(KEY, "baseClass");
            map.put("Value", page.getBaseClass());

            allObjects.add(map);

            map = new HashMap<String, Object>();
            map.put(KEY, "pageTitle");
            for (Entry<String, String> eachPage : page.getAllPageTitles().entrySet()){
                map.put(eachPage.getKey(), eachPage.getValue());
            }

            allObjects.add(map);
            
            for (Entry<String, HtmlElement> eachElement : page.getElements().entrySet()) {
                map = new HashMap<String, Object>();
                map.put(KEY, eachElement.getKey());
                for (Entry<String, String> eachLocale : eachElement.getValue().getLocators().entrySet()) {
                    map.put(eachLocale.getKey(), eachLocale.getValue());
                }
                
                Map<String, HtmlContainerElement> containerElements = eachElement.getValue().getContainerElements();
                if (eachElement.getKey().endsWith(CONTAINER) && !containerElements.isEmpty()) {
                    Map<String, Map<String, String>> containerMap = new HashMap<String, Map<String, String>>();
                    for (Entry<String, HtmlContainerElement> eachContainerElement : containerElements.entrySet()) {
                        Map<String, String> localeMap = new HashMap<String, String>(eachContainerElement.getValue().getLocators());
                        containerMap.put(eachContainerElement.getKey(), localeMap);
                    }
                    map.put(ELEMENTSv2, containerMap);
                }
                allObjects.add(map);
            }
            this.defaultLocale = page.getDefaultLocale();
            this.isPageYamlV2 = true;
        } catch (Exception ex) { //NOSONAR
            // Do Nothing
        } finally {       
            if (input != null) {
                input.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> getDataProviderValues(String locale) {

        HashMap<String, String> instanceMap = new HashMap<String, String>();

        for (Object temp : allObjects) {
            Map<String, String> map = (Map<String, String>) temp;
            try {
                String value = map.get(locale);
                if (value == null) { 
                    value = map.get(this.defaultLocale);
                }
                instanceMap.put(map.get(KEY), value);
            } catch (NullPointerException e) {
            }
        }

        return instanceMap;
    }

    public Map<String, String> getDataProviderValuesForContainer(String containerKey, String locale) {

        Map<String, String> instanceMap = new HashMap<String, String>();

        for (Object temp : allObjects) {
            Map<String, Object> map = (Map<String, Object>) temp;
            try {

                if (!map.get(KEY).equals(containerKey)) {
                    continue;
                }

                // Add child elements of Container
                if (map.containsKey(ELEMENTS)) {
                    List<Map<String, String>> elementList = (ArrayList<Map<String, String>>) map.get(ELEMENTS);
                    for (Map<String, String> eachElementMap : elementList) {
                        String value = eachElementMap.get(locale);
                        if (value == null) {
                            value = eachElementMap.get(this.defaultLocale);
                        }
                        instanceMap.put(eachElementMap.get(KEY), value);
                    }
                } else if (map.containsKey(ELEMENTSv2)) {
                    Map<String, Map<String, String>> elementMap = (Map<String, Map<String, String>>) map
                            .get(ELEMENTSv2);
                    for (Entry<String, Map<String, String>> eachElement : elementMap.entrySet()) {
                        String value = eachElement.getValue().get(locale);
                        if (value == null) {
                            value = eachElement.getValue().get(this.defaultLocale);
                        }
                        instanceMap.put(eachElement.getValue().get(KEY), value);
                    }
                }

            } catch (NullPointerException e) {
            }
        }

        return instanceMap;
    }
}
