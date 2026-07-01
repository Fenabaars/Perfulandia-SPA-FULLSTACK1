$files = Get-ChildItem -Path ".\microservicio-*\pom.xml" -Exclude "*gateway*"
$dependency = @"
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.5.0</version>
		</dependency>
"@

foreach ($f in $files) {
    $content = Get-Content $f.FullName -Raw
    if ($content -notmatch "springdoc-openapi") {
        $content = $content -replace "</dependencies>", "$dependency`n`t</dependencies>"
        Set-Content -Path $f.FullName -Value $content -Encoding UTF8
        Write-Host "Added springdoc to $($f.Name)"
    }
}
