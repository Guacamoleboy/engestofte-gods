# Grundlag: MCP og AI-integration

> Omskrevet fra den tidligere `toolbox.md`. Materialet er undervisningsnoter og skal bruges som fagligt grundlag, ikke som projektspecifikke krav.

MCP Introduction
What is MCP?
MCP (Model Context Protocol) is a standard way for AI assistants to connect to tools, data, and backend logic.

You can think of MCP as:

an interface between an AI model and your software
a common protocol so tools can work across clients (Codex, Claude Desktop, IDE assistants, etc.)
a structured way to expose capabilities like tools, resources, and prompts
In practice, MCP lets an assistant do more than just chat. It can call functions, fetch data, and work with real systems.

Core idea
Without MCP:

AI only knows what is in the prompt
With MCP:

AI can ask connected servers for extra context and actions
Typical flow:

The client (for example Codex or Claude CLI or Desktop) connects to an MCP server.
The server advertises what it can do (tools/resources/prompts).
The AI chooses and calls the right capability when needed.
The server returns structured results.
MCP transports beginners meet first
When people say “MCP versions” in practice, they often mix together two different things:

the MCP specification version
the transport style used between client and server
For beginners, transport style is usually the most concrete place to start.

1)
stdio
(Standard Input/Output)
Local process-to-process communication
Very common for local development and classroom demos
Easy to run: start a command, communicate over stdin/stdout
Best for:

local tools
quick prototypes
no network setup
2)
HTTP
(remote/networked MCP)
In modern MCP docs this is typically Streamable HTTP
Client and server communicate over HTTP
Useful when MCP server runs on another machine or in the cloud
Better fit for shared services and multi-user setups
Best for:

hosted tools
team environments
production-style deployment
3) Older HTTP + SSE style (legacy in some ecosystems)
Earlier setups often used HTTP request/response plus Server-Sent Events (SSE) for streaming
You may still see this in tutorials or older code
In the current spec and SDK docs, Streamable HTTP is the newer direction
SSE-based HTTP transport is mainly relevant when reading or maintaining older examples
Best for:

understanding existing examples
maintaining older MCP integrations
What this means in the Java SDK right now
In the current Java SDK, the main dependency for a simple project is:

io.modelcontextprotocol.sdk:mcp
That package gives you the core SDK plus JSON support out of the box.

For transport choices:

stdio is still the easiest place to start
core SDK supports stdio, SSE, and Streamable HTTP
Spring-specific web transports now live under Spring AI 2.0+, not in the core MCP Java SDK
That means if you build a plain Java demo, stdio is usually the right first step. If you later want Spring WebFlux or WebMVC transport, you should look at the Spring AI MCP modules.

Which one should beginners use first?
Start with stdio.

Why:

simplest mental model
least infrastructure
fastest path to “first working MCP tool”
After that, move to HTTP-based transport when you need remote access or deployment.

MCP is not replacing REST
MCP and REST solve different problems:

REST API: designed for app-to-app communication
MCP: designed for model-to-tool communication
A backend can expose both:

REST for frontend/mobile clients
MCP for AI assistants
Summary
MCP gives AI systems a standard way to use real tools and data.

For beginners:

Learn MCP concepts (tools, resources, prompts)
Start with stdio
Then explore HTTP transports for real deployment
Next steps
For a deeper dive into MCP, check out the MCP Tutorial where we build a simple Java MCP server together.

Read more about MCP in the official documentation:

MCP Documentation
MCP Java SDK Documentation

MCP Tutorial
Build Your First MCP Server in Java (Codex + Claude CLI)
In this tutorial, you will:

build a simple MCP server in Java
expose a tool (add_numbers)
build the project in IntelliJ
connect it to Codex and Claude CLI
call it from natural language
What is MCP (short version)
MCP (Model Context Protocol) lets AI tools like Codex and Claude:

call your code as tools

Think of it like:

REST → for frontend
MCP → for AI
Step 1 — Create a simple Java project
Create a new Maven project in IntelliJ:

Open IntelliJ → New Project
Choose Maven
Set:
GroupId: demo
ArtifactId: mcp-demo
Click Create
Step 2 — Add MCP dependency
Edit your pom.xml:

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>app</groupId>
    <artifactId>mcp-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>25</maven.compiler.source>
        <maven.compiler.target>25</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.modelcontextprotocol.sdk</groupId>
                <artifactId>mcp-bom</artifactId>
                <version>1.1.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>io.modelcontextprotocol.sdk</groupId>
            <artifactId>mcp</artifactId>
        </dependency>
        <dependency>
            <groupId>io.modelcontextprotocol.sdk</groupId>
            <artifactId>mcp-json-jackson3</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.6.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <createDependencyReducedPom>false</createDependencyReducedPom>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>app.McpServerApp</mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
Step 3 — Create your MCP server
Create a file:

src/main/java/demo/McpServerApp.java
Add this code:

package app;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.json.jackson3.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import tools.jackson.databind.json.JsonMapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class McpServerApp {
    private static final String SERVER_NAME = "demo-mcp-server";
    private static final String SERVER_VERSION = "1.0.0";

    private static final String ADD_NUMBERS_SCHEMA = """
            {
              "type": "object",
              "properties": {
                "a": { "type": "integer" },
                "b": { "type": "integer" }
              },
              "required": ["a", "b"]
            }
            """;

    private static final String CURRENT_TIME_SCHEMA = """
            {
              "type": "object",
              "properties": {},
              "additionalProperties": false
            }
            """;

    public static void main(String[] args) {
        var jsonMapper = new JacksonMcpJsonMapper(JsonMapper.builder().build());
        var transportProvider = new StdioServerTransportProvider(jsonMapper);
        var server = McpServer.sync(transportProvider)
                .serverInfo(SERVER_NAME, SERVER_VERSION)
                .capabilities(ServerCapabilities.builder()
                        .tools(true)
                        .build())
                .toolCall(addNumbersTool(jsonMapper), (exchange, request) -> {
                    int a = (int) request.arguments().get("a");
                    int b = (int) request.arguments().get("b");
                    return textResult("Result: " + (a + b));
                })
                .toolCall(currentTimeTool(jsonMapper), (exchange, request) ->
                        textResult(ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)))
                .build();

        Runtime.getRuntime().addShutdownHook(new Thread(server::close));
        awaitShutdown(server::close);
    }

    private static Tool addNumbersTool(JacksonMcpJsonMapper jsonMapper) {
        return Tool.builder()
                .name("add_numbers")
                .description("Add two numbers")
                .inputSchema(jsonMapper, ADD_NUMBERS_SCHEMA)
                .build();
    }

    private static Tool currentTimeTool(JacksonMcpJsonMapper jsonMapper) {
        return Tool.builder()
                .name("current_time")
                .description("Return the current server time in ISO-8601 format")
                .inputSchema(jsonMapper, CURRENT_TIME_SCHEMA)
                .build();
    }

    private static CallToolResult textResult(String text) {
        return CallToolResult.builder()
                .content(List.of(new TextContent(text)))
                .build();
    }

    private static void awaitShutdown(Runnable closeServer) {
        try {
            new CountDownLatch(1).await();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            closeServer.run();
        }
    }
}
This is the newer server style in the official Java SDK:

create a StdioServerTransportProvider
build a sync server with McpServer.sync(...)
register tools with toolCall(...)
define tool input with JSON Schema
What is STDIO?
STDIO = standard input/output

👉 Instead of HTTP, the AI talks to your program directly via:

input stream
output stream
✔ simplest setup ✔ perfect for local tools ✔ still the default way to connect small local MCP servers to CLI tools

Step 4 — Build the project in IntelliJ
Use IntelliJ (not terminal):

Open the Maven tool window (right side)
Expand your project → Lifecycle
Double-click clean
Double-click package
You should now have a jar in:

target/mcp-demo-1.0-SNAPSHOT.jar
Step 5 — Register the MCP server in Codex
Run:

codex mcp add demo-server -- java -jar /Users/jobe/Documents/dat4/mcp-demo/target/mcp-demo-1.0-SNAPSHOT.jar
👉 Replace with your actual absolute path!

Step 6 — Register the same server in Claude CLI
Run:

claude mcp add demo-server \
  --command java \
  --arg -jar \
  --arg /FULL/PATH/TO/target/mcp-demo-1.0-SNAPSHOT.jar
👉 Use the same jar path as above.

Step 7 — Verify in both clients
Codex:

codex mcp list
Claude CLI:

claude mcp list
You should see demo-server in both outputs.

Step 8 — Use your tool from each client
In Codex, try:

Use the add_numbers tool with a=5 and b=7
In Claude CLI, try:

Use the add_numbers tool with a=5 and b=7
👉 Both clients will:

detect your tool
call your Java server
return the result
What just happened?
You built:

AI → MCP → your Java code
Instead of:

Frontend → REST → backend
Key takeaway
MCP is just another interface to your backend logic

Reflection questions
Where does the logic live?
What is the difference between MCP and REST?
Why might AI tools need this instead of HTTP APIs?
⚠️ Common issues
❌ Tool not found
Did you run codex mcp add or claude mcp add?
Did you restart the client?
❌ Nothing happens
Is the .jar path correct?
Does java -jar ... run manually?
❌ Crashes immediately
Check for missing dependencies
Run manually to debug:
java -jar target/mcp-demo-1.0-SNAPSHOT.jar
🧭 What’s next?
You can now:

connect MCP to real services (database, APIs)
combine with Javalin
build AI-powered backend features

Tjekliste
Når du bygger AI-drevne applikationer
Når du bygger en AI-drevet applikation, er det ikke nok bare at sende en prompt til en model og vise svaret. En god løsning kræver, at du tænker over promptdesign, input, output, robusthed, sikkerhed og brugeroplevelse.

Denne side samler en række praktiske ting, du bør huske, når du udvikler applikationer med et LLM-API.

Ordliste
Der introduceres mange fagudtryk og begreber i forbindelse med LLM-integration. Se evt. ordlisten for forklaring af de vigtigste begreber.

Tænk på modellen som en ekstern service
En LLM er typisk en ekstern service, som din applikation kalder via et API. Det betyder, at den skal behandles som alle andre eksterne afhængigheder:

den kan være langsom
den kan fejle
den kan returnere uventede svar
den kan koste penge pr. request
den kan være ustabil, hvis du sender for mange requests på én gang
Derfor skal du ikke kun tænke på, hvad du vil spørge modellen om, men også på hvordan din software håndterer kaldet.

