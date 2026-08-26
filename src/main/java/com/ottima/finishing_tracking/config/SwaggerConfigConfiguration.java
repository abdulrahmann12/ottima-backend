package com.ottima.finishing_tracking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfigConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Finishing Tracking API")
                        .version("1.0")
                        .description(
                                "RESTful API for the Finishing Tracking Platform. " +
                                        "This platform provides a comprehensive solution for managing construction and interior finishing projects, tracking site progress, and coordinating operations.  " +

                                        "Key Features:  " +

                                        "🔐 Authentication & Access Control " +
                                        "- User registration and secure login " +
                                        "- Role-Based Access Control (Admin, Client, Contractor) " +
                                        "- JWT authentication and secure password encryption  " +

                                        "🏗️ Project & Site Management " +
                                        "- Create and manage interior finishing projects " +
                                        "- Track project phases (e.g., plumbing, electrical, painting) " +
                                        "- Timeline monitoring and milestone tracking " +
                                        "- Manage client details and site locations  " +

                                        "📋 Task & Workflow Tracking " +
                                        "- Assign tasks to contractors and workers " +
                                        "- Real-time progress updates and status changes " +
                                        "- Attach site images and inspection reports  " +

                                        "💰 Financial & Expense Tracking " +
                                        "- Manage project budgets and actual expenses " +
                                        "- Generate client invoices and payment tracking " +
                                        "- Contractor payout management  " +

                                        "📦 Material & Resource Management " +
                                        "- Track material orders and consumption " +
                                        "- Supplier management and cost estimation  " +

                                        "📊 Analytics & Dashboard " +
                                        "- Project completion tracking metrics " +
                                        "- Financial summaries and profit margins " +
                                        "- Real-time administrative dashboard  " +

                                        "📧 Notifications & Alerts " +
                                        "- Phase completion and delay alerts " +
                                        "- Payment and invoice reminders " +
                                        "- System activity logs  " +

                                        "⚡ Performance & Infrastructure " +
                                        "- Optimized database queries and caching " +
                                        "- Standardized JSON responses and robust error handling " +
                                        "- Interactive Swagger/OpenAPI documentation"
                        )
                        .contact(new Contact()
                                .name("Abdulrahman Ahmed")
                                .email("abdulraman.ahmedd@gmail.com")
                        )
                );
    }
}