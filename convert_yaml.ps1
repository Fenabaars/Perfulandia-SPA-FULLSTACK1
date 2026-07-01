$files = Get-ChildItem -Path ".\*\src\main\resources\application.properties"
foreach ($f in $files) {
    $content = Get-Content $f.FullName
    $yml = @()
    foreach ($line in $content) {
        if ($line.Trim() -eq "" -or $line.StartsWith("#")) {
            $yml += $line
        } elseif ($line -match "^([^=]+)=(.*)$") {
            $yml += "$($matches[1]): $($matches[2])"
        }
    }
    $yml += ""
    $yml += "---"
    $yml += "spring:"
    $yml += "  config:"
    $yml += "    activate:"
    $yml += "      on-profile: dev"
    
    $newPath = $f.FullName -replace '\.properties$', '.yml'
    $yml | Set-Content $newPath -Encoding UTF8
    Remove-Item $f.FullName
    Write-Host "Converted $($f.Name) to application.yml"
}
