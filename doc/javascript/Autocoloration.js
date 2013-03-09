/**
 * Define style for text inside code markup.
 * For choose a defined style use the id parameter.
 * To use it, in head add the line (Change the relative path if need):
 *    <script src="javascript/Autocoloration.js"></script> 
 * And in body add the onLoad action :
 *    <body onLoad="javascript:launchParser()">
 * If you already have an function script called at this place, just call "launchParser();" at the start of the function
 * Inside your html, if you want insert some java code just do something like :
 *    <code lang="java">
 *    public static void main(Sting[] args)
 *    {
 *         //Print at console : "Hello word !"
 *         System.out.println("Hello word !");
 *    }
 *    </code>
 * No need to specify something else, the script will add automatically the colors
 * For now possible value for lang are : "java" or "xml" 
 * TIP : If you don't want convert all < to &lt; and > to &gt; you can do :
 *    <code lang="java"><!--
 *    public static void main(Sting[] args)
 *    {
 *         //Print at console : "Hello word !"
 *         System.out.println("Hello word !");
 *         ArrayList<String> list = new ArrayList<String>();
 *    }
 *    --></code>
 * And all convertion are done. You can alos do it for XML markup;)
 * Its also possible to include dynamically some code files on using the syntax :
 *    <code lang="java:src/mypackage/path/MyClass.java" />
 * Before the :, describes the launguage, after the : the relaive (or absolute) path to the file to show and colorize
 * Example can find in ../test.html
 * If the code is not syntaxly correct, unexpected result may happen
 */

// **********************************************************************
// *** Settings of colors/bold and italic                             *** 
// *** All are defined by a pair of header and footer                 ***
// *** The header is put before the detected region, and footer after ***
// **********************************************************************

/** Keywords color (Dark red bold) */
var keyword = new Array("<font color=#7F0055><b>", "</b></font>");
/** Strings color (Dark blue) */
var string  = new Array("<font color=#2A00FF>"   , "</font>"    );
/** Comments color (Dark green) */
var comment = new Array("<font color=#3F7F5F>"   , "</font>"    );
/** For part no change colors (Escape characters)*/
var ignore = new Array();

// ************************************
// *** Natures of the coloring rule ***
// ************************************

/** Indicates that each element of the list is a word to colorize with a specific style. Used for keywords in general */
var natureWord = 0;
/** Indicates the list of escapes characters. Its strongly recommended to declare escapes as first rule. Style associate is ignore, so its good idea to use ignore */
var natureEscape = 1;
/** Indicates the each elements of the list is a starting and ending block by itself. For example in Java the strings are delimited by ", the first meeting of " indicates start of string and next " end of string. As you see its the same symbol that start the block and end it.*/
var natureUniqLimit = 2;
/** Indicates a block of element that start by a symbol and finish by one or more different ones. In Java by example / * (start) and * / (unique end) */
var natureBiLimit = 3;
/** Indicates a list of symbol that start a bloc and finish by the end of the line. Like // in Java*/
var natureUtilEndTheLine = 4;

// ********************************
// *** Java language definition *** 
// ********************************

/** Java key words */
var javaKeywords = new Array(
	"public", "protected", "private", "abstract", 
    "class", "interface", "enum", 
    "static", "transient", "final", 
    "if", "else", "for", "while", "do", "return","new", "switch", "case", "break", "continue", "default", "throw", "throws",  "try", "catch", "finally", 
    "boolean", "char", "byte", "short", "int", "double", "float", "double", "void", "true", "false", 
    "package", "import", "extends", "implements",
    "null", "this", "super");
/** Java escape characters */
var javaEscape=new Array("\\");
/** Java String and char limits */
var javaStrings=new Array("'","\"");
/** Java one line comment */
var javaCommentOneLine=new Array("//");
/** Java multi-line comment */
var javaCommentMutiline=new Array(new Array("/*","*/"));

/** Java language description*/
	//				Nature					|	List					|	Style
var java = new Array(
	new Array(	natureEscape,				javaEscape,				ignore),
	new Array(	natureWord,					javaKeywords,			keyword),
	new Array(	natureUniqLimit,			javaStrings,			string),
	new Array(	natureUtilEndTheLine,	javaCommentOneLine,	comment),
	new Array(	natureBiLimit,				javaCommentMutiline,	comment)
);

// *******************************
// *** XML language definition *** 
// *******************************

/** XML escape characters */
var xmlEscape=new Array("\\");
/** XML parameter value */
var xmlStrings=new Array("\"");
/** XML comments*/
var xmlComment=new Array(new Array("&lt;!--","--&gt;"));
/** XML markup*/
var xmlMarkup=new Array(new Array("&lt;","&gt;", " "));

/** XML language description*/
	//				Nature			|	List		|	Style
var xml = new Array(
	new Array(	natureEscape,		xmlEscape,	ignore),
	new Array(	natureUniqLimit,	xmlStrings,	string),
	new Array(	natureBiLimit,		xmlComment,	comment),
	new Array(	natureBiLimit,		xmlMarkup,	keyword)
);

// *****************************
// *** Automatic replacement ***
// *****************************

var replacement = new Array(
	new Array("<","&lt;")
,	new Array(">","&gt;")
);

// *****************
// *** Functions ***
// *****************

/**
 * Select the language to use with code id
 * @param string Code id
 * @return The language to use
 */
function select(string)
{
	switch(string)
	{
		case "java"	:	return java;
		case "xml"	:	return xml;
	}

	return new Array();
}

/**
 * Parse all code markup and colorize them
 */
function parseCodes()
{
	var codes=document.getElementsByTagName("code");
	parseActual(codes, 0);
 }
 
 function parseActual(codes, i)
 {
 	if(i>=codes.length)
 	{
 		return;
 	}
 
	var text = codes[i].innerHTML;
	var index = 0;
	var lang = codes[i].lang;
		
	index = lang.indexOf(":");
		
	if(index>0)
	{
		var client = new XMLHttpRequest();
		client.open('GET', lang.substring(index+1));
		lang = lang.substring(0, index);

		client.onreadystatechange = function() 
		{
			if(this.readyState == this.DONE) 
			{
				text = "\n"+client.responseText;
 				parseMarkupCode(codes[i], text, lang);
 				
 				parseActual(codes, i+1);
			}
		}
		client.send();
	}
	else
	{
		parseMarkupCode(codes[i], text, lang);
		parseActual(codes, i+1);
	}
 }

function parseMarkupCode(makupCode, text, lang)
{
	var language=select(lang);
	if(language.length==0)
	{
		return;
	}

	var temp = "";
	var start = 0;
	var index = 0;

	if(text.substring(0, 4)=="<!--" && 
	   text.substring(text.length-3, text.length)=="-->")
	{
		text=text.substring(4, text.length-3);
	}

	for(j=0; j<replacement.length; j++)
	{
		temp = "";
		start = 0;
		index = text.indexOf(replacement[j][0], 0);
		
		while(index>=0)
		{
			temp += text.substring(start, index) + replacement[j][1]; 
			
			start = index + replacement[j][0].length;
			index = text.indexOf(replacement[j][0], start);
		}
			
		text = temp + text.substring(start);
	}

	var esc = new Array();
	
	for(j=0; j<language.length; j++)
	{
		var nature = language[j][0];
		var list = language[j][1];
		var style = language[j][2];
		var limit ="";
		start = -1;
		var mil = -1;
		var cont = 0;
		var reg=new RegExp("","");
		var rep="";
		index=-1;
		temp="";

		switch(nature)
		{
			case natureEscape :
				esc=list;
			break;
			case natureWord :
				for(k=0; k<list.length; k++)
				{
					reg=new RegExp("([^a-zA-Z0-9_@])("+list[k]+")([^a-zA-Z0-9_@])","g");
					rep="$1"+style[0]+"$2"+style[1]+"$3";
					text=text.replace(reg,rep);
				}
			break;
			case natureUniqLimit :
				start = 0;

				while(start>=0)
				{
					index = text.length;
					for(k=0; k<list.length;k++)
					{
						var ind = text.indexOf(list[k], start);
						if(ind>=0 && index>ind)
						{
							index = ind; 
							limit = list[k];
						}
					}

					if(index>=text.length)
					{
						temp += text.substring(start);
						break;
					}

					mil = index;
					cont=esc.length;

					while(true)
					{
						index = text.indexOf(limit, index+limit.length);

						if(index<0)
						{
							break;
						}

						cont=esc.length;
						for(l=0; l<esc.length; l++)
						{
							if(index-esc[l].length<0 || text.substring(index-esc[l].length, index) != esc[l])
							{
								cont --;
							}
						}

						if(cont<=0)
						{
							break;
						}
					}
					
					if(index>=0)
					{
						cont = text.indexOf("\n", mil);
						if(cont<index)
						{
							temp += text.substring(start, cont+1);
							start = cont+1;
						}
						else
						{
						   temp += text.substring(start, mil) + style[0] + text.substring(mil, index+limit.length)+style[1];
							start=index+limit.length;
						}						
					}
					else
					{
						temp += text.substring(start);
						start = -1;
					}
				}

				text = temp;
			break;
			case natureUtilEndTheLine :
				for(k=0; k<list.length; k++)
				{
					reg=new RegExp("("+list[k]+".*)\n","g");
					rep=style[0]+"$1"+style[1]+"\n";
					text = text.replace(reg, rep);
				}
			break;
			case natureBiLimit :
				for(k=0; k<list.length; k++)
				{
					temp = "";
					start = 0;
					mil = text.indexOf(list[k][0], start);
					
					while(mil>0 && text.charAt(mil-1)=='>')
					{
						mil = text.indexOf(list[k][0], mil+list[k][0].length);
					}

					while(mil>=0)
					{
						index = text.indexOf(list[k][1], mil+list[k][0].length);
						var ch=1;
						for(m=2; m<list[k].length; m++)
						{
							cont = text.indexOf(list[k][m], mil+list[k][0].length);

							if(index<0 || (cont>=0 && cont<index))
							{
								ch=m;
								index = cont;
							}
						}

						if(index<0)
						{
							break;
						}

						temp += text.substring(start, mil) + style[0] + text.substring(mil, index + list[k][ch].length) + style[1];
						start =  index + list[k][ch].length;

						mil = text.indexOf(list[k][0], start);

						while(mil>0 && text.charAt(mil-1)=='>')
						{
							mil = text.indexOf(list[k][0], mil+list[k][0].length);
						}
					}
					
					text = temp+text.substring(start);
				}
			break;
		}
	}
	
	makupCode.innerHTML="<table border=1 bgcolor=#F0F0FF><tr><td><pre><font face=\"courier\">"+text+"</font></pre></td></tr></table>";
}


/**
 * Launch the parsing
 */
function launchParser()
{
	setTimeout(function() {parseCodes();}, 16); 
}