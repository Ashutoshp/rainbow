package org.sa.rainbow.stitch.lib;

import org.acmestudio.acme.core.IAcmeType;
import org.acmestudio.acme.core.type.IAcmeFloatingPointValue;
import org.acmestudio.acme.core.type.IAcmeIntValue;
import org.acmestudio.acme.element.IAcmeElementInstance;
import org.acmestudio.acme.element.property.IAcmeProperty;
import org.acmestudio.acme.element.property.IAcmePropertyValue;
import org.acmestudio.acme.model.DefaultAcmeModel;

public abstract class SwimUtils {
	
	public static int dimmerFactorToLevel(double dimmer, int dimmerLevels, double dimmerMargin) {
		int level = 1 + (int) Math.round((dimmer - dimmerMargin) * (dimmerLevels - 1) / (1.0 - 2 * dimmerMargin));
		return level;
	}

	public static double dimmerLevelToFactor(int level, int dimmerLevels, double dimmerMargin) {
		double factor = dimmerMargin + (1.0 - 2 * dimmerMargin) * (level - 1.0) / (dimmerLevels - 1.0);
		return factor;
	}
	
	/**
	 * Find the element with the minimum value of the property "property"
	 * @param set
	 * @return element
	 */
	public static <E> double minOverProperty(String property, java.util.Set<E> set) {
		double minValue = Double.MAX_VALUE;

		for (E e : set) {
			if (!(e instanceof IAcmeElementInstance<?, ?>)) {
				continue;
			}
			double value = 0;
			IAcmeProperty prop = ((IAcmeElementInstance<?, ?>) e).getProperty(property);
			IAcmeType type = prop.getType();
			IAcmePropertyValue val = prop.getValue();
			if (type == DefaultAcmeModel.defaultIntType()) {
				value = ((IAcmeIntValue) val).getValue();
			} else if (type == DefaultAcmeModel.defaultFloatType()) {
				value = ((IAcmeFloatingPointValue) val).getDoubleValue();
			}
			if (value < minValue) {
				minValue = value;
			}
		}
		return minValue;
	}

	/**
	 * Find the element with the maximum value of the property "property"
	 * @param set
	 * @return element
	 */
	public static <E> double maxOverProperty(String property, java.util.Set<E> set) {
		double maxValue = -Double.MAX_VALUE;

		for (E e : set) {
			if (!(e instanceof IAcmeElementInstance<?, ?>)) {
				continue;
			}
			double value = 0;
			IAcmeProperty prop = ((IAcmeElementInstance<?, ?>) e).getProperty(property);
			IAcmeType type = prop.getType();
			IAcmePropertyValue val = prop.getValue();
			if (type == DefaultAcmeModel.defaultIntType()) {
				value = ((IAcmeIntValue) val).getValue();
			} else if (type == DefaultAcmeModel.defaultFloatType()) {
				value = ((IAcmeFloatingPointValue) val).getDoubleValue();
			}
			if (value > maxValue) {
				maxValue = value;
			}
		}
		return maxValue;
	}
}
