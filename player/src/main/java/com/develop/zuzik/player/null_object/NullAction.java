package com.develop.zuzik.player.null_object;

import com.develop.zuzik.player.interfaces.Action;

/**
 * User: zuzik
 * Date: 8/18/16
 */
public class NullAction implements Action {

	public static final NullAction INSTANCE = new NullAction();

	private NullAction() {
	}

	@Override
	public void execute() {

	}
}
