echo "RSS Parser for $baseUrl"

println loadResource(baseUrl).findFirst('.//item')