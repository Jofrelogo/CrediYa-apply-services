package com.crediya.apply.api;

import com.crediya.apply.api.config.ApplyPath;
import com.crediya.apply.api.dto.ApplyRequestDTO;
import com.crediya.apply.api.dto.ApplyResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
@Tag(name = "Applys", description = "Operations related to Apply management")
public class RouterRest {

    private final ApplyPath applyPath;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/applys",
                    produces = { "application/json" },
                    method = { org.springframework.web.bind.annotation.RequestMethod.POST },
                    beanClass = Handler.class,
                    beanMethod = "createApply",
                    operation = @Operation(
                            operationId = "createApply",
                            summary = "Create new apply",
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = ApplyRequestDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "User created",
                                            content = @Content(schema = @Schema(implementation = ApplyResponseDTO.class)))
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return RouterFunctions.route()
                .POST(applyPath.getApplys(), handler::createApply)
                .build();

    }
}
