## TO DO:
-Wysy�anie danych telemetrycznych po UDP.
-Odbieranie pojedynczego streamu video.


## W celu uruchomienia 5 tutoriala gstreamera w Android Studio:
1. Pobra� projekt z [repozytorium](https://gitlab.com/eduardoprado/gstreamer-tutorial5") 
2. Nie zmienia� wersji gradle w projekcie, pobra� wersj� 2.3.3
3. Pobra� NDK r15c
4. Przypisa� �cie�k� do odpowiedniego NDK w local.properties. (ndk.dir) zmieni� �cie�k� z tej przypisanej przez Android Studio
5. Pobra� binaria ze [strony](https://gstreamer.freedesktop.org/data/pkg/android/1.8.3/) dla wszystkich architektur. Wypakowa� wszystkie do jednego folderu.
6. Przypisa� �cie�k� do folderu zawieraj�cego binaria gstreamera do `GSTREAMER_ROOT_ANDROID` w pliku `Android.mk`.
7. W przypadku u�ytkowania Windowsa

	w pliku dla ka�dym folderze binarek `share\gst-android\ndk-build\gstreamer-1.0.mk`
	oraz `\include\gmp.h`
		zamieni� `-fuse-ld=gold` na `-fuse-ld=gold.exe`



# �r�d�a:
*https://stackoverflow.com/questions/45044210/gstreamer-examples-in-android-studio
*https://stackoverflow.com/questions/44650470/android-ndk-linker-gstreamer-invalid-linker-name-fuse-ld-gold
*Rafa� Wanjas
