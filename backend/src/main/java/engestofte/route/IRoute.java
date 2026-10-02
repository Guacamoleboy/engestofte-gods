package engestofte.route;

import io.javalin.apibuilder.EndpointGroup;

public interface IRoute {
    EndpointGroup routes();
}