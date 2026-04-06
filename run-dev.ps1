$ErrorActionPreference = 'Stop'

$workspace = Split-Path -Parent $MyInvocation.MyCommand.Path
$nodeDir = 'C:\Program Files\nodejs'

# Ensure Node.js is usable in this session even if VS Code/terminal PATH hasn't reloaded yet.
if (Test-Path -LiteralPath (Join-Path $nodeDir 'node.exe')) {
  $env:Path = "$nodeDir;" + $env:Path
}

function Require-Command([string]$name, [string]$installHint) {
  if (-not (Get-Command $name -ErrorAction SilentlyContinue)) {
    Write-Host "Missing required command: $name" -ForegroundColor Red
    Write-Host $installHint -ForegroundColor Yellow
    exit 1
  }
}

Require-Command -name 'java' -installHint 'Install JDK 21 and ensure it is in PATH (java -version).'
Require-Command -name 'mvn'  -installHint 'Install Apache Maven (3.9+) and ensure mvn is in PATH.'
Require-Command -name 'node' -installHint 'Install Node.js LTS (18/20+) and ensure node is in PATH.'
Require-Command -name 'npm'  -installHint 'npm should come with Node.js; reinstall Node.js if npm is missing.'

Write-Host "Java:" -ForegroundColor Green
& java -version 2>&1 | ForEach-Object { Write-Host "  $_" -ForegroundColor Green }
Write-Host "Maven: $(mvn -v | Select-Object -First 1)" -ForegroundColor Green
Write-Host "Node:  $(node -v)" -ForegroundColor Green
Write-Host "npm:   $(npm -v)" -ForegroundColor Green

function Test-UrlOk([string]$url) {
  try {
    Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 3 | Out-Null
    return $true
  } catch {
    return $false
  }
}

function Import-EnvFile([string]$path) {
  if (-not (Test-Path -LiteralPath $path)) {
    return
  }

  Get-Content -LiteralPath $path | ForEach-Object {
    $line = $_.Trim()
    if (-not $line -or $line.StartsWith('#')) { return }

    $pair = $line -split '=', 2
    if ($pair.Count -ne 2) { return }

    $name = $pair[0].Trim()
    $value = $pair[1].Trim().Trim('"')
    if ($name) {
      Set-Item -Path ("Env:" + $name) -Value $value
    }
  }
}

$rootEnv = Join-Path $workspace '.env'
Import-EnvFile $rootEnv

# Backend
$backendHello = 'http://localhost:8080/api/hello'
if (Test-UrlOk $backendHello) {
  Write-Host "Backend already running: $backendHello" -ForegroundColor Green
} else {
  Write-Host "Starting backend on :8080..." -ForegroundColor Yellow
  Start-Process -FilePath 'mvn' -ArgumentList 'spring-boot:run' -WorkingDirectory (Join-Path $workspace 'backend') -WindowStyle Normal
}

# Frontend
$frontendUrl = 'http://localhost:4321'
if (Test-UrlOk $frontendUrl) {
  Write-Host "Frontend already running: $frontendUrl" -ForegroundColor Green
} else {
  Write-Host "Starting frontend on :4321..." -ForegroundColor Yellow
  $frontendDir = Join-Path $workspace 'frontend'
  if (!(Test-Path -LiteralPath (Join-Path $frontendDir 'node_modules'))) {
    Push-Location $frontendDir
    npm install
    Pop-Location
  }
  Start-Process -FilePath 'npm' -ArgumentList 'run','dev','--','--host' -WorkingDirectory $frontendDir -WindowStyle Normal
}

Write-Host "Open: http://localhost:4321" -ForegroundColor Cyan
