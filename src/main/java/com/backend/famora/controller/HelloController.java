package com.backend.famora.controller;

import com.backend.famora.entity.UserProfile;
import com.backend.famora.repository.UserProfileRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {
    @Autowired
    UserProfileRepo repo;

    @GetMapping("health")
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("healthy"));
    }

    @GetMapping("hello")
    public Mono<ResponseEntity<String>> hello() {
        return repo.getUserProfileById("sd3212asd12")
                .map(userProfile ->
                        ResponseEntity.ok("""
                                    <!DOCTYPE html>
                                    <html lang="en">
                                    <head>
                                    <meta charset="UTF-8" />
                                    <meta name="viewport" content="width=device-width, initial-scale=1" />
                                    <title>Project Fambien Status</title>
                                    <style>
                                      body {
                                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                        margin: 1rem;
                                        padding: 0;
                                        background: #f7f9fc;
                                        color: #333;
                                      }
                                      .container {
                                        max-width: 700px;
                                        margin: 0 auto;
                                        background: white;
                                        padding: 1.5rem 2rem;
                                        border-radius: 8px;
                                        box-shadow: 0 0 12px rgba(0,0,0,0.1);
                                      }
                                      h1, h2 {
                                        color: #1a73e8;
                                      }
                                      h1 {
                                        margin-bottom: 0.5rem;
                                      }
                                      p.tech, p.cicd, p.cloud {
                                        margin: 0.25rem 0 1rem 0;
                                      }
                                      section {
                                        margin-bottom: 1.5rem;
                                      }
                                      .phase-title {
                                        font-weight: bold;
                                        font-size: 1.1rem;
                                        margin-bottom: 0.3rem;
                                        color: #444;
                                      }
                                      pre {
                                        background: #272822;
                                        color: #f8f8f2;
                                        padding: 1rem;
                                        border-radius: 6px;
                                        overflow-x: auto;
                                        font-family: 'Courier New', Courier, monospace;
                                      }
                                      @media (max-width: 480px) {
                                        body {
                                          margin: 0.5rem;
                                        }
                                        .container {
                                          padding: 1rem 1.2rem;
                                        }
                                      }
                                    </style>
                                    </head>
                                    <body>
                                      <div class="container">
                                        <h1>Project Fambien running live on AWS</h1>
                                        <p class="tech"><strong>Tech:</strong> Java, Springboot, Amazon DynamoDB</p>
                                        <p class="cicd"><strong>CI-CD:</strong> Github, Docker, Jenkins</p>
                                        <p class="cloud"><strong>Cloud:</strong> ECR, ECS, Route 53, ALB and IAM</p>
                                                            
                                        <section>
                                          <h2>Complete</h2>
                                          <p><strong>Phase 1:</strong> Set-up - Springboot Webflux, DynamoDB, CI-CD and AWS</p>
                                        </section>
                                                            
                                        <section>
                                          <h2>Current Phase</h2>
                                          <p><strong>Phase 2:</strong> Set-up - JWT and develop Rest APIs</p>
                                        </section>
                                                            
                                        <section>
                                          <h2>Next Phase</h2>
                                          <p><strong>Phase 3:</strong> React set-up and continue developing APIs</p>
                                        </section>
                                                            
                                        <section>
                                          <h2>This is the sample data fetched from DynamoDB</h2>
                                          <pre>%s</pre>
                                        </section>
                                      </div>
                                    </body>
                                    </html>
                                    """.formatted(userProfile)));
    }
}
