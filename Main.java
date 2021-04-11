import java.lang.Math;

class Main{
    public static void main(String[] args){
        // 正方形
        QuadrilateralShape square = new QuadrilateralShape(0,0,5,0,5,5,0,5);
        System.out.println(square.getShapeType());
        System.out.println(square.draw());
        // 長方形
        QuadrilateralShape rectangle = new QuadrilateralShape(0,0,8,0,8,5,0,5);
        System.out.println(rectangle.getShapeType());
        System.out.println(rectangle.draw());
        // 平行四辺形
        QuadrilateralShape parallelogram1 = new QuadrilateralShape(0,0,2,2,2,6,0,4);
        System.out.println(parallelogram1.getShapeType());
        System.out.println(parallelogram1.draw());
        QuadrilateralShape parallelogram2 = new QuadrilateralShape(0,0,4,0,6,2,2,2);
        System.out.println(parallelogram2.getShapeType());
        System.out.println(parallelogram2.draw());
        // 台形
        QuadrilateralShape trapezoid = new QuadrilateralShape(
            new Line(new Point(0,0), new Point(6,0)),
            new Line(new Point(6,0), new Point(4,2)),
            new Line(new Point(4,2), new Point(2,2)),
            new Line(new Point(2,2), new Point(0,0))
        );
        System.out.println(trapezoid.getShapeType());
        System.out.println(trapezoid.draw());
    }
}

class Point{
    public double x; // x軸上の点
    public double y; // y軸上の点

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
}

class Line{
    public Point startPoint; // 線の始点
    public Point endPoint;   // 線の終点

    public Line(Point startPoint, Point endPoint){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
}

class QuadrilateralShape{
    public Line lineAB; // 線分AB
    public Line lineBC; // 線分BC
    public Line lineCD; // 線分CD
    public Line lineDA; // 線分DA

    public QuadrilateralShape(  double ax, double ay, double bx, double by, 
                                double cx, double cy, double dx, double dy){
        this.lineAB = new Line(new Point(ax, ay), new Point(bx, by));
        this.lineBC = new Line(new Point(bx, by), new Point(cx, cy));
        this.lineCD = new Line(new Point(cx, cy), new Point(dx, dy));
        this.lineDA = new Line(new Point(dx, dy), new Point(ax, ay));
    }

    public QuadrilateralShape( Line lineAB, Line lineBC, Line lineCD, Line lineDA){
        this.lineAB = lineAB;
        this.lineBC = lineBC;
        this.lineCD = lineCD;
        this.lineDA = lineDA;
    }

    // 1辺の長さを返す
    public double getLength(Line line){
        return Math.sqrt( Math.pow(line.endPoint.x - line.startPoint.x, 2) 
                        + Math.pow(line.endPoint.y - line.startPoint.y, 2));
    }

    // 四角形の周囲の長さを返す
    public double getPerimeter(){
        return  getLength(this.lineAB) + 
                getLength(this.lineBC) + 
                getLength(this.lineCD) + 
                getLength(this.lineDA);
    }

    // ベクトルの内積を返す
    public double getInnerProduct(Line line1, Line line2){
        double x1 = line1.endPoint.x - line1.startPoint.x;
        double y1 = line1.endPoint.y - line1.startPoint.y;
        double x2 = line2.endPoint.x - line2.startPoint.x;
        double y2 = line2.endPoint.y - line2.startPoint.y;
        
        return  x1 * x2 + y1 * y2;
    }

    // 頂点の角度を返す
    public double getAngle(String angleString){
        Line line1;
        Line line2;

        switch(angleString){
            case "BAD":
                line1 = this.lineAB;
                line2 = this.lineDA;
                break;
            case "ABC":
                line1 = this.lineAB;
                line2 = this.lineBC;
                break;
            case "ADC":
                line1 = this.lineDA;
                line2 = this.lineCD;
                break;
            case "BCD":
                line1 = this.lineBC;
                line2 = this.lineCD;
                break;
            default:
                return 0;
        }
        // 2つのベクトルからナス角を計算
        double cos = getInnerProduct(line1, line2) / (getLength(line1) * getLength(line2));
        // ラジアン → 度
        return 180 - Math.toDegrees((Math.acos(cos)));
    }

    // 2辺が平行かどうか判定する
    public boolean isParallel(Line line1, Line line2){
        double x1 = line1.endPoint.x - line1.startPoint.x;
        double y1 = line1.endPoint.y - line1.startPoint.y;
        double x2 = line2.endPoint.x - line2.startPoint.x;
        double y2 = line2.endPoint.y - line2.startPoint.y;

        return  x1 * y2 - y1 * x2 == 0;
    }

    // 四角形の面積を返す
    public double getArea(){
        double left = 1;
        double right = 0;

        left *= (getPerimeter() / 2 - getLength(this.lineAB));
        left *= (getPerimeter() / 2 - getLength(this.lineBC));
        left *= (getPerimeter() / 2 - getLength(this.lineCD));
        left *= (getPerimeter() / 2 - getLength(this.lineDA));

        right += Math.toRadians(getAngle("BAD"));
        right += Math.toRadians(getAngle("ADC"));
        right /= 2;

        return Math.sqrt(left - right);
    }

    // 四角形の名称を返す
    public String getShapeType(){
        // 各線の長さが0、または隣通しの線が平行 → 四角形ではない
        if( getLength(this.lineAB) == 0 || 
            getLength(this.lineBC) == 0 || 
            getLength(this.lineCD) == 0 || 
            getLength(this.lineDA) == 0 || 
            isParallel(this.lineAB, this.lineBC) || 
            isParallel(this.lineBC, this.lineCD) || 
            isParallel(this.lineCD, this.lineDA) || 
            isParallel(this.lineDA, this.lineAB)) return "not a quadrilateral";

        // 対の線がどちらも平行 → 正方形、ひし形、長方形、平行四辺形に絞られる
        if(isParallel(this.lineAB, this.lineCD) && isParallel(this.lineBC, this.lineDA)){

            // 4辺の長さが同じ → 正方形、ひし形に絞られる
            if( getLength(this.lineAB) == getLength(this.lineBC) && 
                getLength(this.lineBC) == getLength(this.lineCD) && 
                getLength(this.lineCD) == getLength(this.lineDA)){
                // 1角が90度 → 正方形
                if(getAngle("BAD") == 90) return "square（正方形）";
                return "rhombus（ひし形）";
            }
            else{
                // 1角が90度 → 長方形
                if(getAngle("BAD") == 90) return "rectangle（長方形）";
                return "parallelogram（平行四辺形）";
            }
        }// 1対のみ平行 → 台形
        else if(isParallel(this.lineAB, this.lineCD) || 
                isParallel(this.lineBC, this.lineDA)){
            return "trapezoid（台形）";
        }
        else{
            // 隣り合う2辺ずつの長さが同じ → 凧
            if(((getLength(this.lineAB) == getLength(this.lineBC) && 
                getLength(this.lineCD) == getLength(this.lineDA)) || 
                (getLength(this.lineBC) == getLength(this.lineCD) && 
                getLength(this.lineDA) == getLength(this.lineAB))) ) return "kite（凧）";
        }
        return "other（その他）";
    }
    // 長方形を描画する
    public String drawRectangle(){
        String output = "";
        int lenAB = (int)Math.round(getLength(this.lineAB));
        int lenBC = (int)Math.round(getLength(this.lineBC));
        int width = lenAB + 2;
        int height = lenBC + 2;

        System.out.println("width: " + Math.round(getLength(this.lineAB)));
        System.out.println("height: " + Math.round(getLength(this.lineBC)));

        for(int i = 1; i <= height; i++){
            for(int j = 1; j <= width; j++){
                if(i == 1 || i == height){// top & bottom
                    if(j == 1) output += " 　"; // left
                    else if(j == width) output += "\n"; // right
                    else output += i == 1 ? "﹍ " : "﹉ ";
                }
                else{
                    if(j == 1 || j == width) output += j == 1 ? "｜ " : "｜\n"; // left & right
                    else output += " 　";
                }
            }
        }
        return output;
    }

    // 平行四辺形を描画する
    public String drawParallelogram(){
        String output = "";
        int lenAB = (int)Math.round(getLength(this.lineAB));
        int lenBC = (int)Math.round(getLength(this.lineBC));
        int width = lenAB + 2;
        int height = lenBC + 2;

        System.out.println("width: " + lenAB);
        System.out.println("height: " + lenBC);

        // lineABがx軸に平行かどうか
        if(this.lineAB.endPoint.y - this.lineAB.startPoint.y == 0){
            width += lenBC;

            for(int i = 1; i <= height; i++){
                for(int j = 1; j <= width; j++){
                    if(i == 1){// top
                        if(j <= lenBC) output += " 　"; // left
                        else if(j == width - 1){// right
                            output += "\n";
                            break;
                        }
                        else output += "﹍ ";
                    }
                    else if(i == height){// bottom
                        if(j <= lenAB) output += "﹉ "; // left
                        else if(j == width) output += "\n"; // right
                        else output += " 　";
                    }
                    else{
                        if(j == width - i - lenAB) output += "／ "; // left
                        else if(j == width - i){// right
                            output += "／\n";
                            break;
                        }
                        else output += " 　";
                    }
                }
            }
        }
        else{
            height += lenAB;

            for(int i = 1; i <= height; i++){
                for(int j = 1; j <= width; j++){
                    if(j == 1){ // left
                        if(i <= lenAB || i >= height - 1) output += " 　"; // top & bottom
                        else output += "｜ ";
                    }
                    else if(j == width){ // right
                        if(i <= lenBC) output += "｜\n"; // top
                        else output += "\n";
                    }
                    else{
                        if(j == width - i) output += "／ ";
                        else if(j == height - i) output += "／ ";
                        else output += " 　";
                    }
                }
            }
        }
        return output;
    }

    // 台形を描画する
    public String drawTrapezoid(){
        String output = "";
        int lenAB = (int)Math.round(getLength(this.lineAB));
        int lenCD = (int)Math.round(getLength(this.lineCD));
        int width = lenAB;
        int height = (lenAB - lenCD) / 2 + 2;

        System.out.println("bottomLen: " + Math.round(getLength(this.lineAB)));
        System.out.println("topLen: " + Math.round(getLength(this.lineCD)));

        for(int i = 1; i <= height; i++){
            for(int j = 1; j <= width; j++){
                if(i == 1){// top
                    if(j == width) output += "\n"; // right
                    else if(j > height - 2 && j <= height - 2 + lenCD) output += "﹍ "; // middle
                    else output += " 　";
                }
                else if(i == height){// bottom
                    output += "﹉ ";
                }
                else{
                    if(j == height - i) output += "／ "; // left
                    else if(j == width - height + i + 1){// right
                        output += "＼\n";
                        break;
                    }
                    else output += " 　";
                }
            }
        }
        return output;
    }

    public String draw(){
        double angleBAD = Math.round(getAngle("BAD"));
        double angleBCD = Math.round(getAngle("BCD"));

        System.out.println("angleBAD: " + (int)angleBAD);
        System.out.println("angleBCD: " + (int)angleBCD);

        // 図形を描く条件
        if(angleBAD == 45){
            if(angleBCD == 45){
                return drawParallelogram(); // 平行四辺形
            }
            return drawTrapezoid(); // 台形
        }
        else if(angleBAD == 90){
            return drawRectangle(); // 正方形、長方形
        }
        return "実装されていません。";
    }
}