package P1_Calculator;

public class CalculatorService {
    private double num1, num2;
    private char mathSymbol;

    public void setNum1(double num1) {
        this.num1 = num1;
    }

    public void setNum2(double num2) {
        this.num2 = num2;
    }

    public void setMathSymbol(char mathSymbol) {
        this.mathSymbol = mathSymbol;
    }

    public char getMathSymbol() {
        return mathSymbol;
    }

    public double add(){
        return this.num1 + this.num2;
    }

    public double subtract(){
        return this.num1 - this.num2;
    }

    public double multiply(){
        return this.num1 * this.num2;
    }

    public double divide(){
        return this.num1/this.num2;
    }
}
