package com.dci.intellij.dbn.language.psql.structure;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.language.common.element.util.ElementTypeAttribute;
import com.dci.intellij.dbn.language.common.psi.BasePsiElement;
import com.dci.intellij.dbn.language.common.psi.IdentifierPsiElement;
import com.dci.intellij.dbn.object.common.DBObjectType;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.intellij.ide.util.treeView.smartTree.Group;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PSQLStructureViewModelGrouper implements Grouper {
    private ActionPresentation actionPresentation = new ActionPresentationData("Object type", "", Icons.ACTION_GROUP);

    private static final Collection<Group> EMPTY_GROUPS = new ArrayList<Group>(0);

    @NotNull
    public Collection<Group> group(AbstractTreeNode abstractTreeNode, Collection<TreeElement> treeElements) {
        Map<DBObjectType, Group> groups = null;
        if (abstractTreeNode.getValue() instanceof PSQLStructureViewElement) {
            PSQLStructureViewElement structureViewElement = (PSQLStructureViewElement) abstractTreeNode.getValue();
            if (structureViewElement.getValue() instanceof BasePsiElement) {

                for (TreeElement treeElement : treeElements) {
                    if (treeElement instanceof PSQLStructureViewElement) {
                        PSQLStructureViewElement element = (PSQLStructureViewElement) treeElement;
                        if (element.getValue() instanceof BasePsiElement) {
                            BasePsiElement basePsiElement = (BasePsiElement) element.getValue();
                            if (!basePsiElement.getElementType().is(ElementTypeAttribute.ROOT)) {
                                BasePsiElement subjectPsiElement = basePsiElement.lookupFirstPsiElement(ElementTypeAttribute.SUBJECT);
                                if (subjectPsiElement instanceof IdentifierPsiElement) {
                                    IdentifierPsiElement identifierPsiElement = (IdentifierPsiElement) subjectPsiElement;
                                    DBObjectType objectType = identifierPsiElement.getObjectType();

                                    if (groups == null) groups = new THashMap<DBObjectType, Group>();
                                    PSQLStructureViewModelGroup group = (PSQLStructureViewModelGroup) groups.get(objectType);
                                    if (group == null) {
                                        group = new PSQLStructureViewModelGroup(objectType);
                                        groups.put(objectType, group);
                                    }
                                    group.addChild(treeElement);
                                }
                            }
                        }
                    }
                }
            }
        }

        return groups == null ? EMPTY_GROUPS : groups.values();
    }

    @NotNull
    public ActionPresentation getPresentation() {
        return actionPresentation;
    }

    @NotNull
    public String getName() {
        return "Object Type";
    }
}
