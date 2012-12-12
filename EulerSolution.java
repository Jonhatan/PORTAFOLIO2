/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;


public class EulerSolution {
    
    static HashMap<Character, Double> mapVariables = new HashMap();
    
    public static void main(String[] agg) {
      EulerSolution ResolverEuler = new EulerSolution();
        
        Scanner lee = new Scanner(System.in);
        System.out.println("Este programa solo resuleve ecuaciones donde las operaciones sean las basicas,Sumar,Restar,Multiplicar,Dividir. El Input del progrma debe ser de la siguiente manera: Ecuacion,x0,y0,xf"+"Sustituyendo las x & y por x0 & y0");
        
        String frase = lee.nextLine().toLowerCase().replaceAll("x0", "x").replaceAll("y0", "y");
        String[] split = frase.split(",");
        
        frase = split[0];
        double xf = Double.parseDouble(split[3]);
        double yf = 0;
        double h = 0.000001;
        
        mapVariables.put('x', Double.parseDouble(split[1]));
        mapVariables.put('y', Double.parseDouble(split[2]));
        mapVariables.put('$', 0.0);
        
        String numero = "";
        int numVar = 65;
        int numRemplazar = 0;
        
        for(int i=frase.length()-1; i>=0; i--) {
            char c = frase.charAt(i);
            
            if(Character.isDigit(c) || c=='.' || (c=='-'&&i-1>=0&&frase.charAt(i-1)=='(')){
                numero = c+numero;
                numRemplazar++;
            }
            else if(!numero.equals("")){
                while(mapVariables.containsKey((char)numVar) || Character.isDigit((char)numVar)){
                    numVar++;
                }
                mapVariables.put((char)numVar, Double.parseDouble(numero));
                
                frase = frase.substring(0, i+1)+((char)numVar)+frase.substring(i+1+numRemplazar);
                
                numero = "";
                numRemplazar = 0;
            }
        }
        
        if(!numero.equals("")){
            while(mapVariables.containsKey((char)numVar) || Character.isDigit((char)numVar)){
                numVar++;
            }
            mapVariables.put((char)numVar, Double.parseDouble(numero));

            frase = ((char)numVar)+frase.substring(numRemplazar);
        }
        
        frase = ResolverEuler.postfijo(frase);

       // System.out.println(ResolverEuler.resolverEcuacion(frase));
        double dydx = 0;
        while(true){
            dydx = ResolverEuler.resolverEcuacion(frase);
            
            yf = mapVariables.remove('y')+dydx*((double)(h));
            
            mapVariables.put('x', mapVariables.remove('x')+((double)(h)));

            mapVariables.put('y', yf);

            if(mapVariables.get('x')>=xf) break;
        }

        System.out.println(yf);
    }
    
    public double resolverEcuacion(String frase){
        ArrayList<Character> arrEliminar = new ArrayList();
        
        int numVar = 65;
        char ultimoCaracter = ' ';
        for(int i=0; i<frase.length(); i++) {
            boolean entre = false;
            double res = 0;
            char c = frase.charAt(i);
            
            if(c=='-'){
                entre = true;
                res = mapVariables.get(frase.charAt(i-2)) - mapVariables.get(frase.charAt(i-1));
            }
            else if (c=='+'){
                entre = true;
                res = mapVariables.get(frase.charAt(i-2)) + mapVariables.get(frase.charAt(i-1));
            }
            else if (c=='*'){
                entre = true;
                res = mapVariables.get(frase.charAt(i-2)) * mapVariables.get(frase.charAt(i-1));
            }
            else if (c=='/'){
                entre = true;
                res = mapVariables.get(frase.charAt(i-2)) / mapVariables.get(frase.charAt(i-1));
            }
            
            if(entre){
                while(mapVariables.containsKey((char)numVar) || Character.isDigit((char)numVar)){
                    numVar++;
                }
                ultimoCaracter = (char)numVar;
                mapVariables.put((char)numVar, res);
                
                if(!arrEliminar.contains((char)numVar))arrEliminar.add((char)numVar);

                frase = frase.substring(0, i-2)+((char)numVar)+frase.substring(i+1);
                
                if(frase.length()==2){
                    frase = "$"+frase;
                }
                
                i = -1;
            }
        }
        
        double resultado = mapVariables.get(ultimoCaracter);
        for(char tmpC : arrEliminar) mapVariables.remove(tmpC);
        
        return resultado;
    }

    public String postfijo(String operacion) {
        String output = "";
        Stack<Character> stack = new Stack<Character>();
        for (int i=0; i<operacion.length(); i++) {
            char opThis = operacion.charAt(i);
            if(Character.isDigit(opThis) || (Character.isLetter(opThis))) {
                output += opThis;
            }
            else if(opThis=='(') {
                stack.push(opThis);
            }
            else if(opThis==')') {
                while(!stack.isEmpty()) {
                    char opTop = stack.pop();
                    if(opTop!='(') {
                        output += opTop;
                    }
                    else if(opTop=='(') {
                        break;
                    }
                }
            }
            else if((opThis=='+') || (opThis=='-') || (opThis=='*') || (opThis=='/')) {
                if(stack.isEmpty()) {
                    stack.push(opThis);
                }
                else{
                    while(!stack.isEmpty()) {
                        char opTop = stack.pop();
                        if(opTop=='(') {
                            stack.push(opTop);
                            break;
                        }
                        else{
                            if(valorTop(opTop) < valorThis(opThis)) {
                                stack.push(opTop);
                                break;
                            }
                            else if(valorTop(opTop) >= valorThis(opThis)) {
                                output += opTop;
                            }
                        }
                    }
                    stack.push(opThis);
                }

            }
        }
        while(!stack.empty()) {
            output += stack.pop();
        }
        //System.out.println(output);
        return output;
    }

    public int valorTop(char opTop) {
        int vTop = 0;
        if((opTop=='+') || (opTop=='-')) {
            vTop = 1;
        }
        else if((opTop=='*') || (opTop=='/')) {
            vTop = 2;
        }
        return vTop;
    }

    public int valorThis(char opThis) {
        int vThis = 0;
        if((opThis=='+') || (opThis=='-')) {
            vThis = 1;
        }
        else if((opThis=='*') || (opThis=='/')) {
            vThis = 2;
        }
        return vThis;
    }
}
