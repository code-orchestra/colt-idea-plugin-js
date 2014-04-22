package codeOrchestra.colt.js.plugin.editor;

import codeOrchestra.colt.core.plugin.icons.Icons;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Dima Kruk
 */
public class CountGutterController {
    private final Map<Editor, Map<Integer, RangeHighlighter>> highlighters;
    private final Map<Editor, Map<Integer, CountGutterIconRenderer>> renderers;

    public CountGutterController() {
        highlighters = new HashMap<Editor, Map<Integer, RangeHighlighter>>();
        renderers = new HashMap<Editor, Map<Integer, CountGutterIconRenderer>>();
    }

    public void add(Editor editor, Integer line, int count) {
        Map<Integer, CountGutterIconRenderer> editorRenderers = renderers.get(editor);
        if (editorRenderers == null)
        {
            editorRenderers = new HashMap<Integer, CountGutterIconRenderer>();
            renderers.put(editor, editorRenderers);
        }

        Map<Integer, RangeHighlighter> editorHighlighters = highlighters.get(editor);
        if (editorHighlighters == null)
        {
            editorHighlighters = new IdentityHashMap<Integer, RangeHighlighter>();
            highlighters.put(editor, editorHighlighters);
        }

        RangeHighlighter highlighter = editorHighlighters.get(line);
        if(highlighter ==null) {
            highlighter = editor.getMarkupModel().addLineHighlighter(line, HighlighterLayer.FIRST, null);
            editorHighlighters.put(line, highlighter);
        }

        CountGutterIconRenderer renderer = editorRenderers.get(line);
        if (renderer == null)
        {
            renderer = new CountGutterIconRenderer(count);
            editorRenderers.put(line, renderer);
        }

        highlighter.setGutterIconRenderer(renderer);

        renderer.setCount(count);
    }

    public void clear() {
        for (Editor editor: highlighters.keySet()) {
            Map<Integer, RangeHighlighter> rangeHighlighterMap = highlighters.get(editor);
            for (Integer line: rangeHighlighterMap.keySet()) {
                editor.getMarkupModel().removeHighlighter(rangeHighlighterMap.get(line));
            }
            rangeHighlighterMap.clear();
        }
    }

    private class CountGutterIconRenderer extends GutterIconRenderer {

        private int count;

        CountGutterIconRenderer(int count) {
            this.count = count;
        }

        @NotNull
        @Override
        public Icon getIcon() {
            return Icons.getCountIcon(count);
        }

        @Nullable
        @Override
        public String getTooltipText() {
            return String.valueOf(count);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            CountGutterIconRenderer that = (CountGutterIconRenderer) o;
            return that.count != count;
        }

        @Override
        public int hashCode() {
            return count;
        }

        private int getCount() {
            return count;
        }

        private void setCount(int count) {
            this.count = count;
        }
    }
}
