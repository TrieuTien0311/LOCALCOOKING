# Script tải mssql-jdbc_auth.dll cho Windows Authentication

$url = "https://github.com/microsoft/mssql-jdbc/releases/download/v12.8.1/mssql-jdbc-12.8.1.jre11-preview.jar"
$tempJar = "$env:TEMP\mssql-jdbc.jar"
$extractPath = "$env:TEMP\sqljdbc_auth"
$targetDir = ".\lib"

Write-Host "Downloading JDBC driver..." -ForegroundColor Green
Invoke-WebRequest -Uri $url -OutFile $tempJar

Write-Host "Extracting DLL files..." -ForegroundColor Green
Expand-Archive -Path $tempJar -DestinationPath $extractPath -Force

# Tạo thư mục lib nếu chưa có
if (-not (Test-Path $targetDir)) {
    New-Item -ItemType Directory -Path $targetDir | Out-Null
}

# Copy DLL file (x64 cho Windows 64-bit)
$dllPath = Get-ChildItem -Path $extractPath -Recurse -Filter "mssql-jdbc_auth-*.dll" | Where-Object { $_.FullName -like "*x64*" } | Select-Object -First 1

if ($dllPath) {
    Copy-Item $dllPath.FullName -Destination "$targetDir\mssql-jdbc_auth.dll" -Force
    Write-Host "✓ DLL copied to $targetDir\mssql-jdbc_auth.dll" -ForegroundColor Green
} else {
    Write-Host "✗ DLL not found in archive" -ForegroundColor Red
}

# Cleanup
Remove-Item $tempJar -Force
Remove-Item $extractPath -Recurse -Force

Write-Host "`nDone! Add this to your PATH or copy to System32" -ForegroundColor Yellow
Write-Host "Or add to application.properties:" -ForegroundColor Yellow
Write-Host "spring.datasource.url=...;integratedSecurity=true" -ForegroundColor Cyan
