package codersafterdark.reskillable.api.requirement.logic;

import codersafterdark.reskillable.api.requirement.Requirement;

import javax.annotation.Nonnull;
import java.util.List;

public interface OuterRequirement {
    @Nonnull
    List<Class<? extends Requirement>> getInternalTypes();
}