package jhelp.util.text.grammar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.list.Pair;
import jhelp.util.text.StringExtractor;

/**
 * Represents a grammar.<br>
 * Grammar is a text that describes text rules and capture each rule, to say if an other text respects the grammar.<br>
 * Grammars <b>MUST</b> respects this grammar :<br>
 * &lt;grammar&gt; := &lt;rules&gt;<br>
 * <br>
 * &lt;rules&gt; := &lt;rule&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| &lt;rule&gt; "\n\n" &lt;rules&gt;<br>
 * <br>
 * &lt;rule&gt; := &lt;name&gt; ":=" &lt;definitions&gt;<br>
 * <br>
 * &lt;definitions&gt; := &lt;definition&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| &lt;definition&gt; "\n" "|" &lt;definitions&gt;<br>
 * <br>
 * &lt;definition&gt; := "0"<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| &lt;definition_not_empty&gt;<br>
 * <br>
 * &lt;definition_not_empty&gt; := "\".+\""<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| "\&lt;" &lt;name&gt; "\&gt;"<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| "\".+\"" &lt;definition_not_empty&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| "\&lt;" &lt;name&gt; "\&gt;" &lt;definition_not_empty&gt;<br>
 * <br>
 * &lt;name&gt; := "[a-zA-Z][a-zA-Z0-9_]*"<br>
 * <br>
 * <b>NOTE :</b>The first rule is considered as the main one
 * 
 * @author JHelp
 */
public class Grammar
      implements GrammarConstants
{
   /** Rules list */
   private final List<Rule> rules;

   /***
    * Create a new instance of Grammar
    */
   public Grammar()
   {
      this.rules = new ArrayList<Rule>();
   }

   /**
    * Compute structure of text for a specific rule
    * 
    * @param rule
    *           Rule to follow
    * @param text
    *           Text tested
    * @return Created structure
    * @throws GrammarException
    *            If text don't respect the rule
    */
   private Structure computeStructure(Rule rule, final String text) throws GrammarException
   {
      final List<Pair<SimpleDefinition, Matcher>> list = rule.searchMatchingDefinition(text);
      Structure structure;
      int count;
      Element element;
      final String ruleName = rule.getName();

      for(final Pair<SimpleDefinition, Matcher> pair : list)
      {
         structure = new Structure(ruleName, false);
         count = pair.element1.getElementCount();

         for(int i = 0; i < count; i++)
         {
            element = pair.element1.getElement(i);

            if(element.isReference() == true)
            {
               rule = this.getRule(element.toString());

               if(rule == null)
               {
                  structure = null;
                  Debug.println(DebugLevel.VERBOSE, "No corresponding rule for ", element.toString());
                  break;
               }

               try
               {
                  structure.addChild(this.computeStructure(rule, pair.element2.group(element.groupName())));
               }
               catch(final Exception exception)
               {
                  structure = null;
                  Debug.println(DebugLevel.VERBOSE, "Add child failed : ", exception);
                  break;
               }
            }
            else if(Element.EMPTY == element)
            {
               if(text.isEmpty() == true)
               {
                  structure.addChild(new Structure("", true));
               }
               else
               {
                  structure = null;
                  Debug.println(DebugLevel.VERBOSE, "Text not empty but : ", text);
                  break;
               }
            }
            else
            {
               structure.addChild(new Structure(pair.element2.group(element.groupName()), true));
            }
         }

         if(structure != null)
         {
            return structure;
         }
      }

      throw new GrammarException(-1, "Stream not respect the grammar. Rule failed : <" + rule.getName() + "> : " + text);
   }

   /**
    * Parse a definition for a specific rule
    * 
    * @param rule
    *           Rule where add the definition
    * @param definition
    *           Definition to parse
    * @param line
    *           Line number in grammar
    * @throws GrammarException
    *            If definition if not a valid definition
    */
   private void parseDefinition(final Rule rule, final String definition, final int line) throws GrammarException
   {
      final StringExtractor stringExtractor = new StringExtractor(definition);
      stringExtractor.setCanReturnEmptyString(false);
      String element = stringExtractor.next();
      final Pattern patternReference = Pattern.compile(GrammarConstants.REGEX_REFERENCE);
      Matcher matcher;

      if(element != null)
      {
         final SimpleDefinition simpleDefinition = new SimpleDefinition();
         rule.addDefinition(simpleDefinition);

         do
         {
            matcher = patternReference.matcher(element);

            if(matcher.matches() == true)
            {
               simpleDefinition.addElement(new ElementReference(matcher.group(GrammarConstants.GROUP_NAME)));
            }
            else if(GrammarConstants.REGEX_EMPTY.equals(element) == true)
            {
               simpleDefinition.addElement(Element.EMPTY);
            }
            else
            {
               simpleDefinition.addElement(new ElementRegularExpression(element));
            }

            element = stringExtractor.next();
         }
         while(element != null);
      }
   }

   /**
    * Add a rule to grammar
    * 
    * @param rule
    *           Rule to add
    */
   public void addRulle(final Rule rule)
   {
      if(rule == null)
      {
         throw new NullPointerException("rule musn't be null");
      }

      this.rules.add(rule);
   }

   /**
    * Append to grammar the grammar rules contains inside the given stream
    * 
    * @param inputStream
    *           Stream to parse
    * @throws GrammarException
    *            If stream not a valid grammar rule definition OR input/output issue
    */
   public void append(final InputStream inputStream) throws GrammarException
   {
      int lineCount = 0;
      try
      {
         final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
         String line = bufferedReader.readLine();
         Rule rule = null;
         final Pattern patternAffectation = Pattern.compile(GrammarConstants.REGEX_AFFECTATION);
         final Pattern patternChoice = Pattern.compile(GrammarConstants.REGEX_CHOICE);
         Matcher matcher;

         while(line != null)
         {
            lineCount++;
            line = line.trim();

            if((line.length() > 0) && (line.charAt(0) != '#'))
            {
               matcher = patternAffectation.matcher(line);

               if(matcher.matches() == true)
               {
                  rule = new Rule(matcher.group(GrammarConstants.GROUP_NAME));
                  this.rules.add(rule);
                  this.parseDefinition(rule, matcher.group(GrammarConstants.GROUP_DEFINITION), lineCount);
               }
               else
               {
                  matcher = patternChoice.matcher(line);

                  if(matcher.matches() == true)
                  {
                     if(rule == null)
                     {
                        throw new GrammarException(lineCount, "Must declare a rule before any choice");
                     }

                     this.parseDefinition(rule, matcher.group(GrammarConstants.GROUP_DEFINITION), lineCount);
                  }
                  else
                  {
                     throw new GrammarException(lineCount, "Invalid Grammar");
                  }
               }
            }

            line = bufferedReader.readLine();
         }
      }
      catch(final GrammarException exception)
      {
         throw exception;
      }
      catch(final Exception exception)
      {
         throw new GrammarException(lineCount, "Failed to parse the grammar", exception);
      }
   }

   /**
    * Clear the grammar.<br>
    * Grammar will not have any rule
    */
   public void clear()
   {
      for(final Rule rule : this.rules)
      {
         rule.clear();
      }

      this.rules.clear();
   }

   /**
    * Compute the structure of a stream in respect of current grammar.<br>
    * Remember that the first rule is the main one
    * 
    * @param inputStream
    *           Stream to parse
    * @return Structure of the given stream
    * @throws GrammarException
    *            If stream not valid for the grammar OR on any input/output issue
    */
   public Structure extractStructure(final InputStream inputStream) throws GrammarException
   {
      if(this.rules.isEmpty() == true)
      {
         throw new GrammarException(-1, "The grammar haven't at least one rule !");
      }

      try
      {
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
         String line = bufferedReader.readLine();
         StringBuilder stringBuilder = new StringBuilder();

         while(line != null)
         {
            stringBuilder.append(line);
            stringBuilder.append(' ');

            line = bufferedReader.readLine();
         }

         bufferedReader.close();
         bufferedReader = null;
         final String text = stringBuilder.toString();
         stringBuilder = null;
         return this.computeStructure(this.rules.get(0), text);
      }
      catch(final GrammarException exception)
      {
         throw exception;
      }
      catch(final Exception exception)
      {
         throw new GrammarException(-1, "Failed on parsing file", exception);
      }
   }

   /**
    * Obtain a rule
    * 
    * @param ruleIndex
    *           Rule index
    * @return The rule
    */
   public Rule getRule(final int ruleIndex)
   {
      return this.rules.get(ruleIndex);
   }

   /**
    * Obtain rule by name
    * 
    * @param name
    *           Rule name
    * @return The rule OR {@code null} if no rule with this name exists
    */
   public Rule getRule(final String name)
   {
      for(final Rule rule : this.rules)
      {
         if(rule.getName().equals(name) == true)
         {
            return rule;
         }
      }

      return null;
   }

   /**
    * Number of rule
    * 
    * @return Number of rule
    */
   public int getRuleCount()
   {
      return this.rules.size();
   }
}