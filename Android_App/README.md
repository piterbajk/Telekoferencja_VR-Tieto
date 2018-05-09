## TO DO:
-Wysy³anie danych telemetrycznych po UDP.
-Odbieranie pojedynczego streamu video.


## W celu uruchomienia 5 tutoriala gstreamera w Android Studio:
1. Pobraæ projekt z [repozytorium](https://gitlab.com/eduardoprado/gstreamer-tutorial5") 
2. Nie zmieniaæ wersji gradle w projekcie, pobraæ wersjê 2.3.3
3. Pobraæ NDK r15c
4. Przypisaæ œcie¿kê do odpowiedniego NDK w local.properties. (ndk.dir) zmieniæ œcie¿kê z tej przypisanej przez Android Studio
5. Pobraæ binaria ze [strony](https://gstreamer.freedesktop.org/data/pkg/android/1.8.3/) dla wszystkich architektur. Wypakowaæ wszystkie do jednego folderu.
6. Przypisaæ œcie¿kê do folderu zawieraj¹cego binaria gstreamera do `GSTREAMER_ROOT_ANDROID` w pliku `Android.mk`.
7. W przypadku u¿ytkowania Windowsa

	w pliku dla ka¿dym folderze binarek `share\gst-android\ndk-build\gstreamer-1.0.mk`
	oraz `\include\gmp.h`
		zamieniæ `-fuse-ld=gold` na `-fuse-ld=gold.exe`



# ród³a:
*https://stackoverflow.com/questions/45044210/gstreamer-examples-in-android-studio
*https://stackoverflow.com/questions/44650470/android-ndk-linker-gstreamer-invalid-linker-name-fuse-ld-gold
*Rafa³ Wanjas
