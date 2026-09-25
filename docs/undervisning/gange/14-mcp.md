# MCP

> Kilde fra undervisningsmaterialet. Struktureret fra den tidligere nummererede fil.


AIDA Fall 2026/14-MCP

## Praktisk
**Gang:** 14 (2/10)

**Tema:** MCP

## Indhold

Miniworkshop om MCP, Model Context Protocol, og hvordan værktøjer, data og eksterne systemer kan forbindes med AI-assistenter.

## Output / portfolio`r`n`r`n Kort refleksion over hvordan MCP kan bruges i jeres projekt eller i en anden AI-drevet applikation.

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