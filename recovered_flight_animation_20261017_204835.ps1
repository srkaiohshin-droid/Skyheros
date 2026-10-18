package org.example;

public class Main {
    public static void main(String[] args) {
        // Classe de exemplo; o mod Skyheros possui sua propria entrada.
    }
}

$ErrorActionPreference = "Stop"

$Project = "C:\Users\srkai\Documents\Load mods mcp\Skyheros"
$Assets = Join-Path $Project "src\main\resources\assets\skyheros"

$GeoFile = Join-Path $Assets "geo\flight_rig.geo.json"
$AnimFile = Join-Path $Assets "animations\flight_rig.animation.json"
$AnimBlockbench = Join-Path $Assets "animations\skyheros\flight_rig.animation.json"

$Utf8NoBom = New-Object System.Text.UTF8Encoding($false)

if (-not (Test-Path $GeoFile)) {
$GeoFile = Join-Path $Assets "geo\skyheros\flight_rig.geo.json"
        }

        if (-not (Test-Path $AnimFile)) {
$AnimFile = Join-Path $Assets "animations\skyheros\flight_rig.animation.json"
        }

        if (-not (Test-Path $GeoFile)) { throw "Geometria ativa não encontrada." }
        if (-not (Test-Path $AnimFile)) { throw "Animações ativas não encontradas." }

$Geo = Get-Content $GeoFile -Raw | ConvertFrom-Json
$GeoDef = @($Geo.'minecraft:geometry')[0]
$GeoNames = @($GeoDef.bones | ForEach-Object { $_.name })

foreach ($Name in @("root", "body", "head", "right_arm", "left_arm", "right_leg", "left_leg")) {
        if ($GeoNames -notcontains $Name) {
        throw "O rig atual não contém o osso esperado '$Name'. Nenhuma animação foi modificada."
        }
        }

$Stamp = Get-Date -Format "yyyyMMdd_HHmmss"
$Backup = Join-Path $Project "backups\reference_hover_$Stamp"
New-Item -ItemType Directory -Force -Path $Backup | Out-Null

Copy-Item $AnimFile (Join-Path $Backup "flight_rig.animation.json")

if (Test-Path $AnimBlockbench) {
Copy-Item $AnimBlockbench (Join-Path $Backup "flight_rig_blockbench.animation.json")
}

Write-Host "`n=== 1/4: USANDO A REFERENCIA CC0 ===" -ForegroundColor Cyan
Write-Host "Fonte dos ângulos:"
Write-Host "https://raw.githubusercontent.com/McVader34/Serious-Player-Animations-Template-Resource-Pack/main/player_animation/falling.json"

function New-SineTrack {
    param(
            [double]$Duration,
        [double[]]$Base,
        [double[]]$Amplitude,
        [double]$Phase = 0,
        [double]$Step = 0.1
    )

    if ($Base.Count -ne 3 -or $Amplitude.Count -ne 3) {
        throw "Base e amplitude precisam ter três eixos."
    }

    $Track = [ordered]@{}
    $Count = [int][Math]::Round($Duration / $Step)

    for ($i = 0; $i -le $Count; $i++) {
        $T = [Math]::Round($i * $Step, 1)
        $Angle = (2.0 * [Math]::PI * $T / $Duration) + $Phase

        $Values = @()

        for ($Axis = 0; $Axis -lt 3; $Axis++) {
            $Value = [double]$Base[$Axis] +
                    ([double]$Amplitude[$Axis] * [Math]::Sin($Angle))

            $Values += [Math]::Round($Value, 4)
        }

        $Key = $T.ToString(
                "0.0",
                [System.Globalization.CultureInfo]::InvariantCulture
        )

        $Track[$Key] = @($Values)
    }

    return ,$Track
}

function New-HoverBone {
    param(
            [double]$Duration,
        [double[]]$RotationBase,
        [double[]]$RotationAmplitude,
        [double]$RotationPhase = 0,
        [double[]]$PositionBase = @(),
        [double[]]$PositionAmplitude = @(),
        [double]$PositionPhase = 0
    )

    $Bone = [ordered]@{}

    $Bone["rotation"] = New-SineTrack `
    -Duration $Duration `
    -Base $RotationBase `
    -Amplitude $RotationAmplitude `
    -Phase $RotationPhase

    if ($PositionBase.Count -eq 3 -and $PositionAmplitude.Count -eq 3) {
        $Bone["position"] = New-SineTrack `
        -Duration $Duration `
        -Base $PositionBase `
        -Amplitude $PositionAmplitude `
        -Phase $PositionPhase
    }

    return ,$Bone
}

Write-Host "`n=== 2/4: MONTANDO A POSE DE PAIRAR ===" -ForegroundColor Cyan

$D = 3.2
$B = [ordered]@{}

# Corpo vertical: remove a inclinação exagerada de -55 graus.
        $B["root"] = New-HoverBone `
        -Duration $D `
        -RotationBase @(0, 0, 0) `
        -RotationAmplitude @(0.7, 0.7, 0.3) `
        -PositionBase @(0, 0, 0) `
        -PositionAmplitude @(0, 0.16, 0)

$B["body"] = New-HoverBone `
        -Duration $D `
        -RotationBase @(0, 0, 0) `
        -RotationAmplitude @(0.5, 0.5, 0.4) `
        -RotationPhase 0.4

$B["head"] = New-HoverBone `
        -Duration $D `
        -RotationBase @(4, 0, 0) `
        -RotationAmplitude @(0.8, 0.5, 0.3) `
        -RotationPhase 0.8

        # Valores de referência do falling.json:
        # rightArm = [-34.76905, 4.29357, 68.65518]
        # leftArm  = [-19.84230, -2.55868, -72.05239]
        #
        # Os sinais do eixo Z são opostos aos da versão anterior do hover.

        $B["right_arm"] = New-HoverBone `
        -Duration $D `
        -RotationBase @(-34.76905, 4.29357, 68.65518) `
        -RotationAmplitude @(0.8, 0.7, 1.0) `
        -RotationPhase 0.15

$B["left_arm"] = New-HoverBone `
        -Duration $D `
        -RotationBase @(-19.84230, -2.55868, -72.05239) `
        -RotationAmplitude @(0.8, 0.7, 1.0) `
        -RotationPhase 0.55

        # Pernas ligeiramente afastadas, seguindo a referência.
        $B["right_leg"] = New-HoverBone `
        -Duration $D `
        -RotationBase @(0, 0, 9) `
        -RotationAmplitude @(3.0, 0.6, 0.8) `
        -RotationPhase 0.3

$B["left_leg"] = New-HoverBone `
        -Duration $D `
        -RotationBase @(0, 0, -7.5) `
        -RotationAmplitude @(2.5, 0.6, 0.8) `
        -RotationPhase 1.0

$Hover = [ordered]@{
    animation_length = $D
    loop = $true
    bones = $B
}

Write-Host "`n=== 3/4: SUBSTITUINDO SOMENTE O PAIRAR ===" -ForegroundColor Cyan

$Doc = Get-Content $AnimFile -Raw | ConvertFrom-Json
$UpdatedAnimations = [ordered]@{}

foreach ($Property in $Doc.animations.PSObject.Properties) {
    if ($Property.Name -ne "animation.skyheros.hover") {
        $UpdatedAnimations[$Property.Name] = $Property.Value
    }
}

$UpdatedAnimations["animation.skyheros.hover"] = $Hover

foreach ($Name in @(
                 "animation.skyheros.takeoff",
    "animation.skyheros.fly_forward",
                 "animation.skyheros.landing"
        )) {
        if ($null -eq $UpdatedAnimations[$Name]) {
        throw "Não encontrei '$Name'. Nenhum arquivo foi salvo."
        }
        }

$NewDoc = [ordered]@{
    format_version = $Doc.format_version
    animations = $UpdatedAnimations
}

$Json = $NewDoc | ConvertTo-Json -Depth 100
$null = $Json | ConvertFrom-Json

[System.IO.File]::WriteAllText($AnimFile, $Json, $Utf8NoBom)

if (Test-Path $AnimBlockbench) {
        [System.IO.File]::WriteAllText($AnimBlockbench, $Json, $Utf8NoBom)
}

Write-Host "Pairar atualizado; as outras três animações foram preservadas." -ForegroundColor Green

Write-Host "`n=== 4/4: CONFERINDO O RESULTADO ===" -ForegroundColor Cyan

$Final = Get-Content $AnimFile -Raw | ConvertFrom-Json
        $FinalHover = $Final.animations.'animation.skyheros.hover'

Write-Host "Duração: $($FinalHover.animation_length) segundos"
Write-Host "Loop: $($FinalHover.loop)"

foreach ($BoneName in @("root", "body", "head", "right_arm", "left_arm", "right_leg", "left_leg")) {
        if ($null -eq $FinalHover.bones.PSObject.Properties[$BoneName]) {
        throw "O osso '$BoneName' não foi escrito corretamente."
        }
        }

Write-Host "Sete ossos da pose de pairar validados." -ForegroundColor Green
Write-Host "Backup: $Backup"
Write-Host "`nExecute agora o Script 2." -ForegroundColor Cyan
