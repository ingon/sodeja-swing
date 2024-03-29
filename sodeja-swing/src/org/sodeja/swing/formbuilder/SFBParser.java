package org.sodeja.swing.formbuilder;

import static org.sodeja.parsec.combinator.ParsecCombinators.apply;
import static org.sodeja.parsec.combinator.ParsecCombinators.oneOrMoreSep;
import static org.sodeja.parsec.combinator.ParsecCombinators.thenParser;
import static org.sodeja.parsec.combinator.ParsecCombinators.thenParser3;
import static org.sodeja.parsec.combinator.ParsecCombinators.thenParser4;
import static org.sodeja.parsec.combinator.ParsecCombinators.thenParser4Cons13;
import static org.sodeja.parsec.combinator.ParsecCombinators.zeroOrMore;
import static org.sodeja.parsec.standart.StandartParsers.alphaDigitsUnderscore;
import static org.sodeja.parsec.standart.StandartParsers.literal;

import java.util.List;

import org.sodeja.collections.ListUtils;
import org.sodeja.functional.Function1;
import org.sodeja.functional.Function2;
import org.sodeja.functional.Function3;
import org.sodeja.functional.Function4;
import org.sodeja.parsec.Parser;
import org.sodeja.parsec.semantic.AbstractSemanticParser;

class SFBParser extends AbstractSemanticParser<String, List<FormObject>>{
	
	private String currentPackage;
	
	private Parser<String, String> TEXT = alphaDigitsUnderscore("TEXT");

	private Parser<String, FormObjectField> FIELD = 
		thenParser4Cons13("FIELD", TEXT, literal(":"), TEXT, literal(";"), FormObjectField.class);
	
	private Parser<String, List<FormObjectField>> FIELDS = zeroOrMore("FIELDS", FIELD);
	
	private Parser<String, FormObject> OBJECT = thenParser4("OBJECT", TEXT, literal("{"), FIELDS, literal("}"), 
		new Function4<FormObject, String, String, List<FormObjectField>, String>() {
			@Override
			public FormObject execute(String p1, String p2, List<FormObjectField> p3, String p4) {
				return new FormObject(currentPackage + p1, p3);
			}});
	
	private Parser<String, List<FormObject>> OBJECTS = zeroOrMore("OBJECTS", OBJECT);
	
	private Parser<String, List<String>> PACKAGE_NAME = oneOrMoreSep("PACKAGE_NAME", TEXT, literal("."));
	
	private Parser<String, String> PACKAGE = thenParser3("PACKAGE", literal("package"), PACKAGE_NAME, literal(";"), 
		new Function3<String, String, List<String>, String>() {
			@Override
			public String execute(String p1, List<String> p2, String p3) {
				currentPackage = ListUtils.foldl(p2, "", new Function2<String, String, String>() {
					@Override
					public String execute(String p1, String p2) {
						return p1 + p2 + ".";
					}});
				return currentPackage;
			}});
	
	private Parser<String, List<FormObject>> PACKAGE_OBJECTS = thenParser("PACKAGE_OBJECTS", PACKAGE, OBJECTS, 
		new Function2<List<FormObject>, String, List<FormObject>>() {
			@Override
			public List<FormObject> execute(String p1, List<FormObject> p2) {
				return p2;
			}});
	
	private Parser<String, List<FormObject>> ROOT = apply("ROOT_APPLY", zeroOrMore("ROOT", PACKAGE_OBJECTS), 
		new Function1<List<FormObject>, List<List<FormObject>>>() {
			@Override
			public List<FormObject> execute(List<List<FormObject>> p) {
				return ListUtils.flattern(p);
			}});

	@Override
	protected Parser<String, List<FormObject>> getParser() {
		return ROOT;
	}
}
