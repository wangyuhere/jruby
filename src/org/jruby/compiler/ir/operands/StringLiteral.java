package org.jruby.compiler.ir.operands;

import java.util.List;
import org.jruby.compiler.ir.targets.JVM;
import org.jruby.util.ByteList;
import org.jruby.RubyString;
import org.jruby.runtime.DynamicScope;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.compiler.ir.representations.InlinerInfo;

/**
 * Represents a literal string value.
 * 
 * This is not an immutable literal because I can gsub!, 
 * for example, and modify the contents of the string.
 * This is not like a Java string.
 */
public class StringLiteral extends Operand {
    // SSS FIXME: Pick one of bytelist or string, or add internal conversion methods to convert to the default representation

    final public ByteList bytelist;
    final public String   string;

    public StringLiteral(ByteList val) {
        bytelist = val;
        string = bytelist.toString();
    }
    
    public StringLiteral(String s) {
        bytelist = ByteList.create(s); string = s;
    }

    @Override
    public boolean hasKnownValue() {
        return true;
    }

    @Override
    public void addUsedVariables(List<Variable> l) {
        /* Do nothing */
    }

    @Override
    public Operand cloneForInlining(InlinerInfo ii) {
        return this;
    }

    @Override
    public String toString() {
        return "\"" + string + "\"";
    }

    @Override
    public Object retrieve(ThreadContext context, IRubyObject self, DynamicScope currDynScope, Object[] temp) {
        // SSS FIXME: AST interpreter passes in a coderange argument.
        return RubyString.newStringShared(context.getRuntime(), bytelist);
    }

    @Override
    public void compile(JVM jvm) {
        jvm.method().push(bytelist);
    }
}
