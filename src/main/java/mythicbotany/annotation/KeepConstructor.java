package mythicbotany.annotation;

import java.lang.annotation.*;

/**
 * The signature of a constructor marked with {@code @KeepConstructor} must be kept
 * in all non-abstract subclasses. There might be additional constructors but you must
 * provide one with the exact signature as the annotated one as the reflection relys on it.
 */
@Target({ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface KeepConstructor {
}
