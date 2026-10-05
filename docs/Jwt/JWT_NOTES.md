# 🔑 Test Secret Generation

For testing purposes, you can quickly generate a random secret (64 bytes) encoded in Base64.

## 🖥️ PowerShell Command (Windows)

```powershell
[Convert]::ToBase64String((1..64 | ForEach-Object {Get-Random -Maximum 256}) -as [byte[]])
```

Generates 64 random bytes.  
Encodes them into a Base64 string.  
Useful for quickly creating test secrets or tokens.
