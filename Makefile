.PHONY: dev dev-install dev-run

dev-run: dev-install
	adb shell am start -n com.gdut.library.makemake/com.gdut.library.makemake.MakemakeActivity

dev-install: dev
	adb uninstall com.gdut.library.makemake
	adb install -r bin/makemake-debug.apk

dev:
	ant debug
