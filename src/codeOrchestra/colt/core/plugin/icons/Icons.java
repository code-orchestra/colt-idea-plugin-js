package codeOrchestra.colt.core.plugin.icons;

import javax.swing.*;

/**
 * @author Alexander Eliseyev
 */
public class Icons {

    public static final Icon COLT_ICON_16 = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/colt_16.png"));

    public static Icon getCountIcon(int value) {
        return value < 10 ? COUNT_ICONS[value - 1] : COUNT_ICONS[9];
    }

    public static final Icon COUTN_1 = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/1@2x.png"));
    public static final Icon COUTN_2 = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/2@2x.png"));
    public static final Icon COUTN_3 = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/3@2x.png"));
    public static final Icon COUTN_4 = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/4@2x.png"));
    public static final Icon COUTN_5 = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/5@2x.png"));
    public static final Icon COUTN_6 = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/6@2x.png"));
    public static final Icon COUTN_7 = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/7@2x.png"));
    public static final Icon COUTN_8 = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/8@2x.png"));
    public static final Icon COUTN_9 = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/9@2x.png"));
    public static final Icon COUTN_INFINITY = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/infinity@2x.png"));

    private static final Icon[] COUNT_ICONS = new Icon[]{COUTN_1, COUTN_2, COUTN_3, COUTN_4, COUTN_5, COUTN_6, COUTN_7, COUTN_8, COUTN_9, COUTN_INFINITY};

    public static final Icon ERROR = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/error@2x.png"));

    public static final Icon LIVE_OFF = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/live.png"));
    public static final Icon LIVE_SWITCHING = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/live-switching.png"));
    public static final Icon LIVE_ON = new ImageIcon(Icons.class.getResource("/codeOrchestra/colt/core/plugin/icons/live-selected.png"));

}
