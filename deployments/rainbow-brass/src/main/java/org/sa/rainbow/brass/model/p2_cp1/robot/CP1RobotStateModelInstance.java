package org.sa.rainbow.brass.model.p2_cp1.robot;

import org.sa.rainbow.brass.model.robot.RobotState;
import org.sa.rainbow.brass.model.robot.RobotStateModelInstance;
import org.sa.rainbow.core.models.commands.ModelCommandFactory;

public class CP1RobotStateModelInstance extends RobotStateModelInstance {
	private CP1RobotStateCommandFactory m_commandFactory;
	
	public CP1RobotStateModelInstance(CP1RobotState r, String source) {
		super(r, source);
	}
	
	@Override
	public CP1RobotState getModelInstance() {
		return (CP1RobotState )super.getModelInstance();
	}
	
	@Override
	public ModelCommandFactory<RobotState> getCommandFactory() {
		if (m_commandFactory == null) {
			m_commandFactory = new CP1RobotStateCommandFactory(this);
		}
		return m_commandFactory;
	}
}
