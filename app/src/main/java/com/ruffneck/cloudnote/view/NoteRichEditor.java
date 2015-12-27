package com.ruffneck.cloudnote.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import jp.wasabeef.richeditor.RichEditor;

public class NoteRichEditor extends RichEditor {

    public static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    public NoteRichEditor(Context context) {
        super(context);
        init(null);
    }

    public NoteRichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NoteRichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        setPadding(10, 10, 10, 10);

        if (attrs == null) {
            this.setEditorHeight(200);
            this.setEditorFontSize(20);
            this.setEditorFontColor(Color.BLACK);
            this.setPlaceholder("请输入正文");
            return;
        }

        int editorHeight = attrs.getAttributeIntValue(NAMESPACE, "editorHeight", 200);
        int fontSize = attrs.getAttributeIntValue(NAMESPACE, "fontSize", 20);
        int fontColor = attrs.getAttributeIntValue(NAMESPACE, "fontColor", Color.BLACK);

        this.setEditorHeight(editorHeight);
        this.setEditorFontSize(fontSize);
        this.setEditorFontColor(fontColor);
        this.setPlaceholder("请输入正文");
    }

}
