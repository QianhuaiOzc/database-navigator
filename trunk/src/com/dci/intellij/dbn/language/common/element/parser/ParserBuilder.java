package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.TokenType;
import com.intellij.lang.PsiBuilder;

public class ParserBuilder {
    private PsiBuilder builder;

    public ParserBuilder(PsiBuilder builder) {
        this.builder = builder;
    }

    public void advanceLexer() {
        builder.advanceLexer();
    }

    public PsiBuilder.Marker mark() {
        return builder.mark();
    }

    public String getTokenText() {
        return builder.getTokenText();
    }

    public TokenType getTokenType() {
        return (TokenType) builder.getTokenType();
    }

    public boolean eof() {
        return builder.eof();
    }

    public int getCurrentOffset() {
        return builder.getCurrentOffset();
    }

    public TokenType lookAhead(int steps) {
        return (TokenType) builder.lookAhead(steps);
    }

    public void error(String messageText) {
        builder.error(messageText);
    }
}
