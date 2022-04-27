// Generated from C:/Users/Ryzen 5/Desktop/School/6th_semester/PJP/Compiler\g.g4 by ANTLR 4.10.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link gParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface gVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link gParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(gParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLine(gParser.LineContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(gParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#assigment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssigment(gParser.AssigmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#print}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint(gParser.PrintContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#output}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutput(gParser.OutputContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#read}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRead(gParser.ReadContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(gParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#elseBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseBlock(gParser.ElseBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoop(gParser.LoopContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(gParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code par}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar(gParser.ParContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifier}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(gParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code a}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitA(gParser.AContext ctx);
	/**
	 * Visit a parse tree produced by the {@code comp}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComp(gParser.CompContext ctx);
	/**
	 * Visit a parse tree produced by the {@code or}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(gParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bool}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(gParser.BoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code string}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(gParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mul}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMul(gParser.MulContext ctx);
	/**
	 * Visit a parse tree produced by the {@code concat}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcat(gParser.ConcatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code float}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloat(gParser.FloatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code int}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt(gParser.IntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code not}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(gParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code and}
	 * labeled alternative in {@link gParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(gParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by {@link gParser#datatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatype(gParser.DatatypeContext ctx);
}