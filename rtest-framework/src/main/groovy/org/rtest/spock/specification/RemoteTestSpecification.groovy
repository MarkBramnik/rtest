package org.rtest.spock.specification

import org.junit.runner.RunWith;
import org.rtest.framework.RemoteTestRunner;
import org.rtest.api.annotations.RealRunner;
import org.spockframework.runtime.Sputnik;
import spock.lang.Specification;

/**
 * The framework base specification - all specification
 * that want to run remotely should extend from this specification.
 * <p/>
 * Usually one common application spec will extend this one and all the specs will
 * extend from that abstract application spec
 * </p>
 * Created by Mark Bramnik on 19/09/2016.
 */
@RunWith(RemoteTestRunner.class)
@RealRunner(Sputnik.class)
public class RemoteTestSpecification extends Specification {
}