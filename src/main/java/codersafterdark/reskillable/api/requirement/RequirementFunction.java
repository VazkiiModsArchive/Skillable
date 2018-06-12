package codersafterdark.reskillable.api.requirement;

@FunctionalInterface
public interface RequirementFunction<T, R> {
    R apply(T t) throws RequirementException;
}