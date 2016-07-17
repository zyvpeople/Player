package com.develop.zuzik.player.exception;

/**
 * User: zuzik
 * Date: 5/29/16
 */
public class ServiceIsNotDeclaredInManifestException extends RuntimeException {
	public ServiceIsNotDeclaredInManifestException(Class<?> serviceClass) {
		super(String.format("Service '%s' must be declared in Manifest", serviceClass));
	}
}
