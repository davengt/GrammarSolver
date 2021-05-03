//Daven Giftian Tejalaksana
//Sunday, 2 May 2021
//CSE 143
//Instructor: Stuart Reges
//TA: Andrew Cheng
//Assignment #5
//This program manipulates a set of grammar rules (BNF format) that is defined by the user.
//It then generates random elements (non-terminals) of the grammar.

import java.util.*;

public class GrammarSolver {
   private SortedMap<String, List<String[]>> grammarForms; //stores rules for every nonterminals
   
   //Pre: Grammar list should not be empty. (throws IllegalArgumentException if not)
   //Pre: There should not be two or more entries in grammar for the same nonterminal.
      //throws IllegalArgumentException if not.
   //Post: Stores grammar in a convenient way used to generate parts of the grammar later.
      //Method will be passed a grammar as a list of strings.
      //Method should not change the list of strings.
   public GrammarSolver(List<String> grammar) {
      if (grammar.isEmpty()) {
         throw new IllegalArgumentException("Grammar list is empty."); 
      }
      
      grammarForms = new TreeMap<>(); //initializing grammarForms map
      for (String line: grammar) {
         String[] parts = line.split("::=");
         if (grammarForms.containsKey(parts[0])) {
            throw new IllegalArgumentException("Nonterminal has two or more entries.");
         }
         String[] rules = parts[1].split("[|]+");
         List<String[]> listOfRules = new ArrayList<>();
         for (String word: rules) {
            word = word.trim();
            String[] rulePieces = word.split("[ \t]+");
            listOfRules.add(rulePieces);
         }
         grammarForms.put(parts[0], listOfRules);
      }
   }
   
   //Post: returns true if given symbol is a nonterminal of the grammar (Returns false otherwise)
      //Case matters when comparing symbols.
   public boolean grammarContains(String symbol) {
      return grammarForms.containsKey(symbol);
   }
   
   //Pre: Grammar should contain the given non-terminal symbol & times should not be less than 0.
      //throws IllegalArgumentException if not
   //Post: Use grammar to randomly generate given number of occurences (times) of given symbol.
      //Returns result as an array of strings.
      //For any given nonterminal symbol, each of its rules are applied with equal probability.
      //Case matters when comparing symbols.
   public String[] generate(String symbol, int times) {
      if (!grammarContains(symbol) || times < 0) {
         throw new IllegalArgumentException(symbol + " is not found, times: " + times);
      }
      
      String[] results = new String[times];
      for (int i = 0; i < times; i++) {
         results[i] = generateLine(symbol);
      }
      return results;
   }
   
   //A recursive method that is a branch of the generate method.
   //Post: returns a string of terminal word(s) that adheres to the grammar rules of the symbol.
   private String generateLine(String symbol) {
      String grammarResult = "";
      List<String[]> rules = grammarForms.get(symbol);
      Random r = new Random();
      int position = r.nextInt(rules.size());
      String[] determinedRule = rules.get(position);
      for (String word: determinedRule) {
         if (!grammarContains(word)) {
            grammarResult += word + " "; //base case
         } else {
            grammarResult += generateLine(word) + " "; //recursive case
         }
      }
      return grammarResult.trim();
   }
   
   //Post: returns a string representation of the various nonterminal symbols from grammar.
      //The represented nonterminals will be listed in sorted order.
      //Form of string is a sorted, comma-separated list enclosed in square brackets.
      //Example of form: "[<np>,<s>,<vp>]"
   public String getSymbols() {
      return grammarForms.keySet().toString();
   }
}