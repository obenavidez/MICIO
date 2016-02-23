package com.panzyma.nm.viewdialog;

import com.panzyma.nordismobile.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class DialogSeleccionTipoDocumento extends DialogFragment {

	private View view;
	private Spinner cmbTipoDocumentos;
	private Seleccionable eventSeleccionable;
	private Documento documento_seleccionado;

	public enum Documento {
		FACTURA, NOTA_DEBITO, NOTA_CREDITO
	}

	public interface Seleccionable {
		public void onSeleccionarDocumento(Documento document);
	}
	
	public void setEventSeleccionable(Seleccionable listener){
		eventSeleccionable = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.layout_seleccion_tipo_documento, null);
		builder.setView(view);
		builder.setPositiveButton("ACEPTAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				eventSeleccionable.onSeleccionarDocumento(documento_seleccionado);
			}
		});
		builder.setNegativeButton("CANCELAR", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		initComponents();
		return builder.create();
	}

	private void initComponents() {
		cmbTipoDocumentos = (Spinner) view
				.findViewById(R.id.cmb_detalletipodocumento);
		ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this.getActivity(), R.array.tipodocumentos,	android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cmbTipoDocumentos.setAdapter(adapter2);
		//ESTABLECER POR DEFECTO EL DOCUMENTO EN FACTURA
		cmbTipoDocumentos.setSelection(0);
		cmbTipoDocumentos
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentview,
							View selectedItemView, int pos, long id) {
						switch (pos) {
						case 0:
							documento_seleccionado = Documento.FACTURA;
							break;
						case 1:
							documento_seleccionado = Documento.NOTA_DEBITO;
							break;
						case 2:
							documento_seleccionado = Documento.NOTA_CREDITO;
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

}
