# ChatSimplifier

Plugin na zabranovanie spamu v chate

## config.yml

```yaml
char-trash-holder: 4 # kolko znakov po sebe aktivuje zjednodusovanie
block-char: "*" # znak, ktory nahradi zablokovane slovo
blacklist: # zablokovane slova
  - "idiot"
  - "dog"
msg: # spravy
  busy-on: "&4&lBusy &7mod &azapnuty"
  busy-off: "&4&lBusy &7mod &azvypnuty"
  busy-msg: "&7Tento hrac je aktualne zaneprazdneny. Vyskusaj neskor"
```

## Prikazy

* `/busy` - zapne / vypne buzy mode - ludia nemoyu napisat jeho ncim do chatu

## Permissie

* `chatsimplifier.busy` - moznost prikazu `/busy`
* `chatsimplifier.overbusy`- moznost napisat aj cez `/busy`