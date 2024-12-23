import java.awt.*;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean highlightSpecialPoints = false;

    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private Font axisFont;

    public GraphicsDisplay() {
        // Цвет заднего фона области отображения - белый
        setBackground(Color.WHITE);

        // Сконструировать необходимые объекты, используемые в рисовании
        float[] dash = {21, 10, 3, 10, 12, 10, 3, 10, 21, 10};
        graphicsStroke = new BasicStroke(4.0f, BasicStroke.CAP_SQUARE,
                BasicStroke.JOIN_MITER, 22.0f, dash, 0.0f);
        axisStroke = new BasicStroke(3.0f);
        markerStroke = new BasicStroke(2.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
    }

    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
        repaint();
    }

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public void setHighlightSpecialPoints(boolean highlightSpecialPoints) {
        this.highlightSpecialPoints = highlightSpecialPoints;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graphicsData == null || graphicsData.length == 0) return;

        Graphics2D canvas = (Graphics2D) g;

        // Настройки сглаживания
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Отрисовка осей координат
        if (showAxis) paintAxis(canvas);

        // Отрисовка линий графика
        paintGraphics(canvas);

        // Отрисовка маркеров точек
        if (showMarkers) paintMarkers(canvas);
    }

    private void paintAxis(Graphics2D canvas) {
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setFont(axisFont);

        // Центр координат
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Ось X
        canvas.drawLine(0, centerY, getWidth(), centerY);
        // Ось Y
        canvas.drawLine(centerX, 0, centerX, getHeight());

        // Подписи осей
        canvas.drawString("X", getWidth() - 20, centerY - 10);
        canvas.drawString("Y", centerX + 10, 20);
    }

    private void paintGraphics(Graphics2D canvas) {
        canvas.setColor(Color.BLUE);
        canvas.setStroke(graphicsStroke);

        for (int i = 0; i < graphicsData.length - 1; i++) {
            int x1 = translateX(graphicsData[i][0]);
            int y1 = translateY(graphicsData[i][1]);
            int x2 = translateX(graphicsData[i + 1][0]);
            int y2 = translateY(graphicsData[i + 1][1]);
            canvas.drawLine(x1, y1, x2, y2);
        }
    }

    private void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);

        for (Double[] point : graphicsData) {
            int x = translateX(point[0]);
            int y = translateY(point[1]);

            if (highlightSpecialPoints && isSpecialPoint(point)) {
                canvas.setColor(Color.RED);
            } else {
                canvas.setColor(Color.BLUE);
            }

            int[] xPoints = {x, x - 5, x, x + 5};
            int[] yPoints = {y - 5, y, y + 5, y};
            canvas.fillPolygon(xPoints, yPoints, 4);
        }
    }

    private boolean isSpecialPoint(Double[] point) {
        double value = Math.floor(Math.abs(point[1]));
        while (value > 0) {
            if ((value % 10) % 2 == 0) return true;
            value /= 10;
        }
        return false;
    }

    private int translateX(double x) {
        double scale = getWidth() / (getMaxX() - getMinX());
        return (int) ((x - getMinX()) * scale);
    }

    private int translateY(double y) {
        double scale = getHeight() / (getMaxY() - getMinY());
        return getHeight() - (int) ((y - getMinY()) * scale);
    }

    private double getMinX() {
        double minX = graphicsData[0][0];
        for (Double[] point : graphicsData) {
            if (point[0] < minX) minX = point[0];
        }
        return minX;
    }

    private double getMaxX() {
        double maxX = graphicsData[0][0];
        for (Double[] point : graphicsData) {
            if (point[0] > maxX) maxX = point[0];
        }
        return maxX;
    }

    private double getMinY() {
        double minY = graphicsData[0][1];
        for (Double[] point : graphicsData) {
            if (point[1] < minY) minY = point[1];
        }
        return minY;
    }

    private double getMaxY() {
        double maxY = graphicsData[0][1];
        for (Double[] point : graphicsData) {
            if (point[1] > maxY) maxY = point[1];
        }
        return maxY;
    }
}
