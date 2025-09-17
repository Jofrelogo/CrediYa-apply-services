package com.crediya.apply.api;

import com.crediya.apply.api.config.ApplyPath;
import com.crediya.apply.api.dto.ApplyDecisionRequestDTO;
import com.crediya.apply.api.dto.ApplyDecisionResponseDTO;
import com.crediya.apply.api.dto.ApplyRequestDTO;
import com.crediya.apply.api.dto.ApplyResponseDTO;
import com.crediya.apply.model.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
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
                    path = "/api/v1/apply",
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
                                            content = @Content(schema = @Schema(implementation = ApplyResponseDTO.class))),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/apply",
                    produces = { "application/json" },
                    method = { org.springframework.web.bind.annotation.RequestMethod.GET },
                    beanClass = Handler.class,
                    beanMethod = "listForReview",
                    operation = @Operation(
                            operationId = "listApplysForReview",
                            summary = "List applications for manual review",
                            description = "Returns a paginated list of applications with status 'PENDING_REVIEW', 'REJECTED', or 'MANUAL_REVIEW'. Accessible only by users with role ADVISOR.",
                            parameters = {
                                    @Parameter(
                                            name = "page",
                                            in = ParameterIn.QUERY,
                                            description = "Page number (starts at 0)",
                                            required = false,
                                            example = "0"
                                    ),
                                    @Parameter(
                                            name = "size",
                                            in = ParameterIn.QUERY,
                                            description = "Number of records per page",
                                            required = false,
                                            example = "10"
                                    ),
                                    @Parameter(
                                            name = "statuses",
                                            in = ParameterIn.QUERY,
                                            description = "Filter by states (comma separated). Example: PENDING_REVIEW,REJECTED,MANUAL_REVIEW",
                                            required = false,
                                            array = @ArraySchema(schema = @Schema(type = "string"))
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Paginated list of applications",
                                            content = @Content(
                                                    schema = @Schema(implementation = PageResponse.class),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Page 1 Example",
                                                                    value = """
                                                                    {
                                                                      "content": [
                                                                        {
                                                                          "id": "1",
                                                                          "name": "Apply Test",
                                                                          "state": "PENDING_REVIEW",
                                                                          "createdAt": "2025-09-08T20:15:00"
                                                                        }
                                                                      ],
                                                                      "page": 0,
                                                                      "size": 10,
                                                                      "totalElements": 8,
                                                                      "totalPages": 1
                                                                    }
                                                                    """
                                                            )
                                                    }
                                            )
                                    ),
                                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/applies",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = { org.springframework.web.bind.annotation.RequestMethod.PUT },
                    beanClass = Handler.class,
                    beanMethod = "decideApply",
                    operation = @Operation(
                            operationId = "decideApply",
                            summary = "Approve or reject a credit application",
                            description = "Allows an advisor (role: ASESOR) to approve or reject an existing credit application",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = ApplyDecisionRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Credit application successfully updated",
                                            content = @Content(schema = @Schema(implementation = ApplyDecisionResponseDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid request or business validation error"
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Unauthorized - missing or invalid JWT"
                                    ),
                                    @ApiResponse(
                                            responseCode = "403",
                                            description = "Forbidden - only advisors can use this endpoint"
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Credit application not found"
                                    )
                            }
                    )
            )

    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return RouterFunctions.route()
                .POST(applyPath.getSaveApply(), handler::createApply)
                .GET(applyPath.getListApplies(), handler::listForReview)
                .PUT(applyPath.getDecideApply(), handler::decideApply)
                .build();

    }


}
