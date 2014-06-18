package com.dci.intellij.dbn.language.common.element.parser;

import com.dci.intellij.dbn.language.common.ParseException;
import com.dci.intellij.dbn.language.common.TokenType;
import com.dci.intellij.dbn.language.common.element.IdentifierElementType;
import com.dci.intellij.dbn.language.common.element.path.ParsePathNode;
import com.intellij.lang.PsiBuilder;

public class IdentifierElementTypeParser extends AbstractElementTypeParser<IdentifierElementType> {
    public IdentifierElementTypeParser(IdentifierElementType elementType) {
        super(elementType);
    }

    public ParseResult parse(ParsePathNode parentNode, boolean optional, int depth, ParserContext context) throws ParseException {
        ParserBuilder builder = context.getBuilder();
        logBegin(builder, optional, depth);
        TokenType tokenType = builder.getTokenType();
        if (tokenType != null && !tokenType.isChameleon()){
            if (tokenType.isIdentifier()) {
                PsiBuilder.Marker marker = builder.mark();
                builder.advanceLexer();
                return stepOut(marker, depth, ParseResultType.FULL_MATCH, 1, null, context);
            }
            else if (getElementType().isDefinition() || isSuppressibleReservedWord(tokenType, parentNode)) {
                PsiBuilder.Marker marker = builder.mark();
                advanceLexer(parentNode, context);
                return stepOut(marker, depth, ParseResultType.FULL_MATCH, 1, null, context);
            }
        }
        return stepOut(null, depth, ParseResultType.NO_MATCH, 0, null, context);
    }  
}