package com.artem.learning.server.couchdb;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * TODO: Document!
 *
 * @author artem on 3/2/16.
 */
public class ViewDefinition {

    public final String name;
    private String mapFunctionFile;
    private String reduceFunctionFile;

    public ViewDefinition(@NotNull String name, @NotNull String mapFunctionFile, String reduceFunctionFile) {
        this.name = name;
        this.mapFunctionFile = mapFunctionFile;
        this.reduceFunctionFile = reduceFunctionFile;
    }

    public String getMapFunction() {
        return readFileContents(mapFunctionFile);
    }

    public String getReduceFunction() {
        return (reduceFunctionFile == null) ? null : readFileContents(reduceFunctionFile);
    }

    private String readFileContents(@NotNull String file) {
        try {
            return IOUtils.toString(new ClassPathResource(file).getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read contents of " + file);
        }
    }
}
