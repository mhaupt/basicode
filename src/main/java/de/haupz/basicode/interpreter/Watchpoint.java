package de.haupz.basicode.interpreter;

import de.haupz.basicode.ast.ExpressionNode;

/**
 * A watchpoint in a BASICODE program.
 *
 * @param id the watchpoint's ID; numbering starts at 1.
 * @param condition the BASICODE condition that, when {@code true} after the execution of a statement, will trigger
 *                  this watchpoint.
 */
public record Watchpoint(int id, ExpressionNode condition) {}
