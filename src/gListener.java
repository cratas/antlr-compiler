// Generated from C:/Users/Ryzen 5/Desktop/School/6th_semester/PJP/Compiler\g.g4 by ANTLR 4.10.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link gParser}.
 */
public interface gListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link gParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(gParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(gParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#line}.
	 * @param ctx the parse tree
	 */
	void enterLine(gParser.LineContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#line}.
	 * @param ctx the parse tree
	 */
	void exitLine(gParser.LineContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(gParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(gParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#assigment}.
	 * @param ctx the parse tree
	 */
	void enterAssigment(gParser.AssigmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#assigment}.
	 * @param ctx the parse tree
	 */
	void exitAssigment(gParser.AssigmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#print}.
	 * @param ctx the parse tree
	 */
	void enterPrint(gParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#print}.
	 * @param ctx the parse tree
	 */
	void exitPrint(gParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#output}.
	 * @param ctx the parse tree
	 */
	void enterOutput(gParser.OutputContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#output}.
	 * @param ctx the parse tree
	 */
	void exitOutput(gParser.OutputContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#read}.
	 * @param ctx the parse tree
	 */
	void enterRead(gParser.ReadContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#read}.
	 * @param ctx the parse tree
	 */
	void exitRead(gParser.ReadContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(gParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(gParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#elseBlock}.
	 * @param ctx the parse tree
	 */
	void enterElseBlock(gParser.ElseBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#elseBlock}.
	 * @param ctx the parse tree
	 */
	void exitElseBlock(gParser.ElseBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#loop}.
	 * @param ctx the parse tree
	 */
	void enterLoop(gParser.LoopContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#loop}.
	 * @param ctx the parse tree
	 */
	void exitLoop(gParser.LoopContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(gParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(gParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code par}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterPar(gParser.ParContext ctx);
	/**
	 * Exit a parse tree produced by the {@code par}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitPar(gParser.ParContext ctx);
	/**
	 * Enter a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(gParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(gParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code a}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterA(gParser.AContext ctx);
	/**
	 * Exit a parse tree produced by the {@code a}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitA(gParser.AContext ctx);
	/**
	 * Enter a parse tree produced by the {@code comp}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterComp(gParser.CompContext ctx);
	/**
	 * Exit a parse tree produced by the {@code comp}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitComp(gParser.CompContext ctx);
	/**
	 * Enter a parse tree produced by the {@code or}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterOr(gParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code or}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitOr(gParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bool}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterBool(gParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bool}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitBool(gParser.BoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code string}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterString(gParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code string}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitString(gParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mul}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterMul(gParser.MulContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mul}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitMul(gParser.MulContext ctx);
	/**
	 * Enter a parse tree produced by the {@code concat}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterConcat(gParser.ConcatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code concat}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitConcat(gParser.ConcatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code float}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterFloat(gParser.FloatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code float}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitFloat(gParser.FloatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code int}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterInt(gParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code int}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitInt(gParser.IntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code not}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterNot(gParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code not}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitNot(gParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code and}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterAnd(gParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code and}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitAnd(gParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by {@link gParser#datatype}.
	 * @param ctx the parse tree
	 */
	void enterDatatype(gParser.DatatypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link gParser#datatype}.
	 * @param ctx the parse tree
	 */
	void exitDatatype(gParser.DatatypeContext ctx);
}