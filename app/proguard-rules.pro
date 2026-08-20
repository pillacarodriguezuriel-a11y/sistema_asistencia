# Conservar metadatos usados por reflexión y generación de código.
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Agregar reglas específicas de Room, Hilt, CameraX, ML Kit y Apache POI cuando
# esas dependencias se incorporen. No mantener paquetes completos sin medir el
# resultado: el objetivo release es minimizar el APK mediante R8.

