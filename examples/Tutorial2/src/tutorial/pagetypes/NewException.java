package tutorial.pagetypes;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.html.BasePage;

public class NewException extends BasePage {
    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public IBinding getExceptionBinding() {
        return exceptionBinding;
    }

    public void setExceptionBinding(IBinding exceptionBinding) {
        this.exceptionBinding = exceptionBinding;
    }

    private IBinding exceptionBinding;
    private Throwable exception;
}
