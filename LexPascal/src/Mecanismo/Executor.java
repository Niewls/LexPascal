package Mecanismo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Executor {

    private final String captureComment = "\\/\\/.*|\\(\\*(?:.|\\s)*\\*\\)";
    private final String captureNumbers = "(?<!\\w)(?:-?\\d+(?:\\.\\d+)?)(?!\\w)";
    private final String captureLiteral = "'(?:[^']|'')*'";
    private final String captureWords   = "\\w+";
    private final String captureCharacters = "(?::=|>=|<=|<>|>|<|=|\\+|\\-|\\*|\\/|[:;,.()\\[\\]{}])";
    private final String captureIdentifier = "^[A-Za-z][A-Za-z0-9_]*$";
    private int end = 0;
    private String capture;
    private BufferedReader reader;
    private ArrayList<String> bufferPrimario;
    private ArrayList<String> bufferSecundario;

    private HashMap<String, Token> tabelaSimbolosPrograma;

    private boolean IsNumber(String valor)
    {
        Pattern pattern = Pattern.compile(this.captureNumbers);
        Matcher matcher = pattern.matcher(valor);
        if (matcher.find() == true){
            return true;
        }
        return false;
    }

    private boolean IsLiteral(String valor)
    {
        Pattern pattern = Pattern.compile(this.captureLiteral);
        Matcher matcher = pattern.matcher(valor);
        if (matcher.find() == true){
            return true;
        }
        return false;
    }

    private boolean IsCharacter(String valor)
    {
        Pattern pattern = Pattern.compile(this.captureCharacters);
        Matcher matcher = pattern.matcher(valor);
        if (matcher.find() == true){
            return true;
        }
        return false;
    }

    private boolean IsIdentifier(String valor)
    {
        Pattern pattern = Pattern.compile(this.captureIdentifier);
        Matcher matcher = pattern.matcher(valor);
        if (matcher.find() == true){
            return true;
        }
        return false;
    }

    public void CarregarArquivo(){
        System.out.println("----------------------------------------");
        System.out.println("##### Carregar Arquivo Pascal #####");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o diretório do arquivo: ");
        String diretorio = scanner.next();
        System.out.print("Digite o nome do arquivo (com extensão .pas): ");
        String nomeArquivo = scanner.next();
        String caminhoCompleto = diretorio + "/" + nomeArquivo;
        this.CarregarBufferPrimario(caminhoCompleto);
        scanner.close();        
    }

    public void CarregarArquivo(String caminhoCompleto){
        System.out.println("----------------------------------------");
        System.out.println("##### Carregar Arquivo Pascal #####");
        this.CarregarBufferPrimario(caminhoCompleto);
    }

    private void CarregarBufferPrimario(String caminhoCompleto){
        this.reader = null;
        try {
            this.reader = new BufferedReader(new FileReader(caminhoCompleto));
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public void ProcessarBufferPrimario(){
        this.bufferPrimario = new ArrayList<>();
        try {
            String linha;
            while ((linha = this.reader.readLine()) != null) {
                bufferPrimario.add(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Erro ao fechar o arquivo: " + e.getMessage());
                }
            }
        }
    }

    public void ImprimirBufferPrimario(){
        System.out.println("----------------------------------------");
        System.out.println("##### Conteúdo do Buffer primário: #####");
        for (String texto : this.bufferPrimario) {
            System.out.println(texto);
        }
        System.out.println("----------------------------------------");        
    }

    public void ProcessarBufferSecundario()
    {        
        this.capture = captureComment.concat("|")
            .concat(captureNumbers).concat("|")
            .concat(captureLiteral).concat("|")
            .concat(captureWords).concat("|")
            .concat(captureCharacters);

        this.bufferSecundario = new ArrayList<>();

        Pattern pattern = Pattern.compile(this.capture);

        for (String texto : bufferPrimario) {
            Matcher matcher = pattern.matcher(texto);
            while(matcher.find()){
                String lexema = matcher.group();
                if (this.bufferSecundario.contains(lexema) == false){
                    this.bufferSecundario.add(lexema);
                }
            }
        }
        bufferSecundario.removeIf(value -> value.startsWith("//") || value.startsWith("(*"));
    } 
    
    public void ImprimirBufferSecundario(){
        System.out.println("----------------------------------------");
        System.out.println("##### Conteúdo do Buffer secundário: #####");
        for (String texto : this.bufferSecundario) {
            System.out.println(texto);
        }
        System.out.println("----------------------------------------");        
    }

    public ArrayList<String> getSimbolos()
    {
        ArrayList<String> simbolos = new ArrayList<>(Arrays.asList(
            ".", ",", ";", ":", "::", ":=", "..", "'", "\"",
            "(", ")", "()", "[", "]", "[]", "{", "}", "{}",
            "=", ">", "<", ">=", "<=", "<>", "+", "-", "*", "/", "^", "%"
        ));
        return simbolos;
    }
    public ArrayList<String> getPalavrasReservadas()
    {
        ArrayList<String> palavrasReservadas = new ArrayList<>(Arrays.asList(
    "abstract", "ansichar", "ansistring", "array", "as", "asm", "assembler", "automated", "begin", "boolean",
    "break", "byte", "case", "cdecl", "char", "class", "const", "constructor", "continue", "currency",
    "default", "deprecated", "destructor", "dispose", "div", "do", "double", "downto", "else", "end",
    "exit", "export", "extended", "external", "false", "far", "file", "finalization", "finalized", "finally",
    "for", "forward", "function", "goto", "if", "implementation", "in", "inherited", "initialization", "inline",
    "int64", "integer", "interface", "interrupt", "is", "label", "library", "longint", "longword", "message",
    "mod", "near", "new", "nil", "nodefault", "object", "of", "operator", "overload", "override",
    "packed", "pascal", "platform", "private", "procedure", "program", "property", "protected", "public", "published",
    "raise", "read", "readln", "readonly", "real", "record", "register", "reintroduce", "repeat", "requires",
    "resident", "resourcestring", "safecall", "sealed", "self", "set", "shl", "shortint", "shortstring", "shr",
    "single", "smallint", "stdcall", "strict", "string", "then", "threadvar", "to", "true", "try",
    "type", "uint64", "unicodestring", "unit", "until", "uses", "var", "varargs", "virtual", "while",
    "widechar", "widestring", "with", "word", "write", "writeln", "xor", "and", "or", "not"
));
    
    return palavrasReservadas;

    }

    public void AnalisarMontandoTabelaSimbolos() {
        TabelaSimbolosLinguagem tabelaLinguagem = new TabelaSimbolosLinguagem();
        HashMap<String, Token> tabelaGeral = tabelaLinguagem.getTabela();
        this.tabelaSimbolosPrograma = new HashMap<>();
        ArrayList<String> palavrasReservadas = this.getPalavrasReservadas();
        ArrayList<String> simbolos = this.getSimbolos();
    
        for (String lexema : bufferSecundario) {
            if (palavrasReservadas.contains(lexema)) 
            {
                Token alvo = tabelaGeral.get(lexema);
                this.tabelaSimbolosPrograma.put(lexema, new Token(alvo.getToken(), lexema, alvo.getTipo(), alvo.getDescricao(), ++end)); 
            } 
            else if (this.IsNumber(lexema)) 
            {
                this.tabelaSimbolosPrograma.put(lexema,new Token(tabelaLinguagem.getNumber(), lexema, "Número", "Token de números", ++end));
            } 
            else if (this.IsLiteral(lexema)) 
            {
                this.tabelaSimbolosPrograma.put(lexema, new Token(tabelaLinguagem.getLiteral(), lexema, "Literal", "Token de Strings Literais", ++end));
            } 
            else if (this.IsIdentifier(lexema)) 
            {
                this.tabelaSimbolosPrograma.put(lexema,new Token(tabelaLinguagem.getIdentifier(), lexema, "Identificador", "Token de identificação", ++end));
            }
            else if (this.IsCharacter(lexema) && simbolos.contains(lexema)) 
            {
                Token alvo = tabelaGeral.get(lexema);
                this.tabelaSimbolosPrograma.put(lexema, new Token(alvo.getToken(), lexema, alvo.getTipo(), alvo.getDescricao(), ++end));
            }
        }
    }
    

    public void ImprimirTabelaSimbolosPrograma(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/Archive/teste.txt"))) {
            writer.write("##### Tabela de Símbolos #####\n");

            for (Token token : tabelaSimbolosPrograma.values()) {
                writer.write("Token: " + token.getToken() + "\n");
                writer.write("Lexema: " + token.getLexema() + "\n");
                writer.write("Tipo: " + token.getTipo() + "\n");
                writer.write("Descrição: " + token.getDescricao() + "\n");
                writer.write("Endereço: " + token.getEndereco() + "\n");
                writer.write("------------------------------------\n");
            }
            System.out.println("Arquivo Salvo!");
        }
        catch(IOException e)
        {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }

    
    }

}
