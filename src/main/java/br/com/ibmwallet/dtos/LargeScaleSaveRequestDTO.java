package br.com.ibmwallet.dtos;

import java.util.List;

public record LargeScaleSaveRequestDTO(Long client_id, List<List<String>> dataList) {
}
