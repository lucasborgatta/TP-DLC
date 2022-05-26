package com.utn.dlc.app.models;

public class CalculoPeso {
    int frecuenciaTerminoR; //cantidad de veces que tr aparece en el documento di.
    int frecuenciaTerminoS; //cantidad de veces que ts aparece en el documento di.
    int cantidadDocumentos; //cantidad total de documentos de la base.
    int frecuenciaDocumentosR; //cantidad de documentos en los que aparece el termino tr
    int frecuenciaDocumentosS; //cantidad de documentos en los que aparece el termino ts
    double frecuenciaInversaR; //frecuencia inversa del término tr. (logaritmo del cociente entre N y nr.)
    double frecuenciaInversaS; //frecuencia inversa del término ts. (logaritmo del cociente entre N y ns.)

/*
    public double calcularPeso(Palabra termR, int cantidadPalabrasxDocumento){
        double peso = 0;
        double sumatoria = 0;
        for (int i = 0; i < cantidadPalabrasxDocumento; i++) { //i se inicializa en 0 o en 1????
            sumatoria += Math.pow((this.frecuenciaTerminoS * Math.log10(this.frecuenciaInversaS)), 2);
        }
        sumatoria = Math.sqrt(sumatoria);
        peso = (this.frecuenciaTerminoR * this.frecuenciaInversaR) / sumatoria;
        return peso;
    }

    // Este esta a medio hacer pero va bien encaminado, falta la parte del denominador
    public double calcularPeso2(int tf, int N, int n, int cantidadTerminosDelDocumento) {

        double peso = 0;
        double idf = 0;

        double idfParcial = (double) (N/n);

        idf = Math.log(idfParcial);

        peso = (tf * idf) / cantidadTerminosDelDocumento;

        return peso;
    }

 */
}
