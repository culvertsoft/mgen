/*
 * $Id: JSONParser.java,v 1.1 2006/04/15 14:10:48 platform Exp $
 * Created on 2006-4-15
 */
package se.culvertsoft.mgen.javapack.serialization.mgen2jsonsimple;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.Yytoken;

/**
 * 
 * THIS SOURCE WAS GRABBED FROM JSON-SIMPLE AND IS MODIFIED FOR MGEN.
 * 
 * Modified version of json-simple's JSONParser class to allow us to parse a
 * stream of consecutive json objects. This is done by changing 2 things. First:
 * The lexer is never reset. Second: status==finished is caught before reading
 * the next char in the stream (look-ahead would otherwise lose 1 char "between"
 * each json object).
 * 
 * Parser for JSON text. Please note that JSONParser is NOT thread-safe.
 * 
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public class MGenJSONParser {
	public static final int S_INIT = 0;
	public static final int S_IN_FINISHED_VALUE = 1;// string,number,boolean,null,object,array
	public static final int S_IN_OBJECT = 2;
	public static final int S_IN_ARRAY = 3;
	public static final int S_PASSED_PAIR_KEY = 4;
	public static final int S_IN_PAIR_VALUE = 5;
	public static final int S_END = 6;
	public static final int S_IN_ERROR = -1;

	private MGenYylex lexer = new MGenYylex((Reader) null);
	private Yytoken token = null;
	private int status = S_INIT;

	public MGenJSONParser(final Reader utf8Reader) {
		lexer.yyreset(utf8Reader);
	}

	public void setInput(final Reader utf8Reader) {
		reset();
		lexer.yyreset(utf8Reader);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object parseNext() throws IOException, ParseException {
		reset();
		LinkedList<Object> statusStack = new LinkedList<>();
		LinkedList<Object> valueStack = new LinkedList<>();

		try {
			do {

				// This is the real magic
				if (status == S_IN_FINISHED_VALUE)
					return valueStack.removeFirst();

				nextToken();
				switch (status) {
				case S_INIT:
					switch (token.type) {
					case Yytoken.TYPE_VALUE:
						status = S_IN_FINISHED_VALUE;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(token.value);
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						status = S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(createObjectContainer(null));
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						status = S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(createArrayContainer(null));
						break;
					default:
						status = S_IN_ERROR;
					}// inner switch
					break;

				case S_IN_FINISHED_VALUE:
					if (token.type == Yytoken.TYPE_EOF)
						return valueStack.removeFirst();
					else
						throw new ParseException(
								getPosition(),
								ParseException.ERROR_UNEXPECTED_TOKEN,
								token);

				case S_IN_OBJECT:
					switch (token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						if (token.value instanceof String) {
							String key = (String) token.value;
							valueStack.addFirst(key);
							status = S_PASSED_PAIR_KEY;
							statusStack.addFirst(new Integer(status));
						} else {
							status = S_IN_ERROR;
						}
						break;
					case Yytoken.TYPE_RIGHT_BRACE:
						if (valueStack.size() > 1) {
							statusStack.removeFirst();
							valueStack.removeFirst();
							status = peekStatus(statusStack);
						} else {
							status = S_IN_FINISHED_VALUE;
						}
						break;
					default:
						status = S_IN_ERROR;
						break;
					}// inner switch
					break;

				case S_PASSED_PAIR_KEY:
					switch (token.type) {
					case Yytoken.TYPE_COLON:
						break;
					case Yytoken.TYPE_VALUE:
						statusStack.removeFirst();
						String key = (String) valueStack.removeFirst();
						Map parent = (Map) valueStack.getFirst();
						parent.put(key, token.value);
						status = peekStatus(statusStack);
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						statusStack.removeFirst();
						key = (String) valueStack.removeFirst();
						parent = (Map) valueStack.getFirst();
						List newArray = createArrayContainer(null);
						parent.put(key, newArray);
						status = S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newArray);
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						statusStack.removeFirst();
						key = (String) valueStack.removeFirst();
						parent = (Map) valueStack.getFirst();
						Map newObject = createObjectContainer(null);
						parent.put(key, newObject);
						status = S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newObject);
						break;
					default:
						status = S_IN_ERROR;
					}
					break;

				case S_IN_ARRAY:
					switch (token.type) {
					case Yytoken.TYPE_COMMA:
						break;
					case Yytoken.TYPE_VALUE:
						List val = (List) valueStack.getFirst();
						val.add(token.value);
						break;
					case Yytoken.TYPE_RIGHT_SQUARE:
						if (valueStack.size() > 1) {
							statusStack.removeFirst();
							valueStack.removeFirst();
							status = peekStatus(statusStack);
						} else {
							status = S_IN_FINISHED_VALUE;
						}
						break;
					case Yytoken.TYPE_LEFT_BRACE:
						val = (List) valueStack.getFirst();
						Map newObject = createObjectContainer(null);
						val.add(newObject);
						status = S_IN_OBJECT;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newObject);
						break;
					case Yytoken.TYPE_LEFT_SQUARE:
						val = (List) valueStack.getFirst();
						List newArray = createArrayContainer(null);
						val.add(newArray);
						status = S_IN_ARRAY;
						statusStack.addFirst(new Integer(status));
						valueStack.addFirst(newArray);
						break;
					default:
						status = S_IN_ERROR;
					}// inner switch
					break;
				case S_IN_ERROR:
					throw new ParseException(
							getPosition(),
							ParseException.ERROR_UNEXPECTED_TOKEN,
							token);
				}// switch
				if (status == S_IN_ERROR) {
					throw new ParseException(
							getPosition(),
							ParseException.ERROR_UNEXPECTED_TOKEN,
							token);
				}
			} while (token.type != Yytoken.TYPE_EOF);
		} catch (IOException ie) {
			throw ie;
		}

		throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
	}

	private void nextToken() throws ParseException, IOException {
		token = lexer.yylex();
		if (token == null)
			token = new Yytoken(Yytoken.TYPE_EOF, null);
	}

	@SuppressWarnings("rawtypes")
	private Map createObjectContainer(ContainerFactory containerFactory) {
		if (containerFactory == null)
			return new JSONObject();
		Map m = containerFactory.createObjectContainer();

		if (m == null)
			return new JSONObject();
		return m;
	}

	@SuppressWarnings("rawtypes")
	private List createArrayContainer(ContainerFactory containerFactory) {
		if (containerFactory == null)
			return new JSONArray();
		List l = containerFactory.creatArrayContainer();

		if (l == null)
			return new JSONArray();
		return l;
	}

	private int peekStatus(LinkedList<Object> statusStack) {
		if (statusStack.size() == 0)
			return -1;
		Integer status = (Integer) statusStack.getFirst();
		return status.intValue();
	}

	private void reset() {
		token = null;
		status = S_INIT;
	}

	private int getPosition() {
		return lexer.getPosition();
	}

}
